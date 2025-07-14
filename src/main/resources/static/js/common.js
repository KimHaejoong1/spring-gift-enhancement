document.addEventListener('DOMContentLoaded', function () {
  // --- 페이지 로드 시 기존 인증 정보 강제 정리 ---
  // HttpOnly 쿠키는 JS로 직접 삭제할 수 없으므로, /logout 엔드포인트를 사용하는 것이 가장 확실합니다.
  // 현재 코드는 localStorage만 제거하며, 토큰 방식 변경으로 사실상 불필요하지만 안전을 위해 남겨둡니다.
  if (window.location.pathname === '/members/login' || window.location.pathname
      === '/members/register') {
    localStorage.removeItem('accessToken');
  }

  // --- 회원가입 및 로그인 처리 ---
  const registerForm = document.getElementById('register-form');
  if (registerForm) {
    registerForm.addEventListener('submit', handleAuthFormSubmit);
  }

  const loginForm = document.getElementById('login-form');
  if (loginForm) {
    loginForm.addEventListener('submit', handleAuthFormSubmit);
  }

  // --- 위시리스트 페이지일 경우, 데이터 로드 함수 실행 ---
  const wishlistBody = document.getElementById('wishlist-body');
  if (wishlistBody) {
    fetchWishlist();
  }

  // --- 이벤트 위임을 사용한 통합 이벤트 리스너 ---
  document.body.addEventListener('click', function (event) {
    // 상세 페이지 이동 로직
    const clickableRow = event.target.closest('.clickable-row');
    if (clickableRow) {
      const link = clickableRow.dataset.link;
      if (link) {
        window.location.href = link;
      }
      return; // 다른 클릭 이벤트와 중복되지 않도록 여기서 종료
    }

    // 위시리스트 추가 또는 삭제 버튼
    if (event.target.classList.contains('add-to-wishlist-btn')
        || event.target.classList.contains('delete-from-wishlist-btn')) {
      handleWishlistAction(event);
    }
  });

  // --- 삭제 확인 ---
  document.querySelectorAll(".delete-form").forEach(form => {
    form.addEventListener("click", function (event) {
      event.stopPropagation();
    });

    form.addEventListener("submit", function (event) {
      if (!confirm("정말 삭제하시겠습니까?")) {
        event.preventDefault();
      }
    });
  });
});

// 로그인/회원가입 폼 제출 비동기 함수
async function handleAuthFormSubmit(event) {
  event.preventDefault();
  const form = event.target;
  const url = form.action;
  const email = form.email.value;
  const password = form.password.value;

  try {
    const response = await fetch(url, {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify({email, password})
    });

    if (response.ok) {
      // 성공 시 HttpOnly 쿠키가 자동으로 브라우저에 저장됩니다.
      const successMsg = (url.includes('register')) ? '회원가입 성공!' : '로그인 성공!';
      alert(successMsg);
      window.location.href = '/members/products';
    } else {
      // 실패 시, 서버로부터 받은 JSON 에러 메시지를 파싱하여 사용합니다.
      const errorData = await response.json();
      const errorMessage = errorData.message || '알 수 없는 오류가 발생했습니다.';
      alert(
          `${(url.includes('register')) ? '회원가입' : '로그인'} 실패: ${errorMessage}`);
    }
  } catch (error) {
    console.error('Error:', error);
    alert('요청 중 오류가 발생했습니다. 서버 상태를 확인해주세요.');
  }
}

// 위시리스트 추가/삭제 비동기 함수
async function handleWishlistAction(event) {
  event.stopPropagation();
  const button = event.target;
  const productId = button.dataset.productId;
  const wishId = button.dataset.wishId;

  // 더 이상 localStorage에서 토큰을 가져오지 않습니다. 브라우저가 자동으로 쿠키를 전송합니다.
  const url = (productId) ? '/api/wishes' : `/api/wishes/${wishId}`;
  const method = (productId) ? 'POST' : 'DELETE';

  if (method === 'DELETE' && !confirm('정말 삭제하시겠습니까?')) {
    return;
  }

  try {
    const response = await fetch(url, {
      method: method,
      headers: {
        'Content-Type': 'application/json',
        // 'Authorization' 헤더는 쿠키 사용으로 인해 더 이상 필요 없습니다.
      },
      body: (productId) ? JSON.stringify({productId: productId}) : null
    });

    if (response.ok) {
      const successMsg = (method === 'POST') ? '위시리스트에 상품을 추가했습니다!'
          : '위시리스트에서 상품을 삭제했습니다.';
      alert(successMsg);
      if (method === 'DELETE') {
        window.location.reload(); // 삭제 후 페이지 새로고침
      }
    } else {
      const errorData = await response.json();
      const errorMessage = errorData.message || '요청에 실패했습니다.';
      alert(`요청 실패: ${errorMessage}`);
    }
  } catch (error) {
    console.error('Error:', error);
    alert('요청 중 오류가 발생했습니다. 서버 상태를 확인해주세요.');
  }
}

// 위시리스트 데이터를 가져와 렌더링하는 함수
async function fetchWishlist() {
  const wishlistBody = document.getElementById('wishlist-body');

  const params = new URLSearchParams(window.location.search);
  const page = params.get('page') || 0;
  const size = params.get('size') || 5;
  const sort = params.get('sort') || 'createdDate,desc';

  try {
    const response = await fetch(
        `/api/wishes?page=${page}&size=${size}&sort=${sort}`);

    if (!response.ok) {
      // fetch 자체가 실패한 것이 아니므로, 서버가 보낸 에러 메시지를 파싱합니다.
      const errorData = await response.json();
      const errorMessage = errorData.message || '위시리스트를 불러오는 데 실패했습니다.';
      throw new Error(errorMessage); // 에러를 발생시켜 catch 블록으로 넘깁니다.
    }

    const items = await response.json();
    wishlistBody.innerHTML = '';

    if (items.length === 0) {
      wishlistBody.innerHTML = '<tr><td colspan="4">위시리스트에 담긴 상품이 없습니다.</td></tr>';
      return;
    }

    items.forEach(item => {
      const row = `
        <tr>
          <td class="clickable-row" data-link="/members/products/${item.product.id}">${item.product.name}</td>
          <td class="clickable-row" data-link="/members/products/${item.product.id}">${item.product.price}원</td>
          <td class="clickable-row" data-link="/members/products/${item.product.id}"><img src="${item.product.imageUrl}" alt="이미지 없음"/></td>
          <td>
            <button class="delete-from-wishlist-btn" data-wish-id="${item.id}">삭제</button>
          </td>
        </tr>
      `;
      wishlistBody.innerHTML += row;
    });

  } catch (error) {
    console.error('Error:', error);
    // catch 블록에서 최종적으로 사용자에게 에러를 표시합니다.
    wishlistBody.innerHTML = `<tr><td colspan="4">오류: ${error.message}</td></tr>`;
  }
}