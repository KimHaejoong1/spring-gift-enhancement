document.addEventListener('DOMContentLoaded', () => {

    // 회원 추가 모달
    const addModal = document.getElementById('addMemberModal');
    const openAddBtn = document.getElementById('openAddMemberModalBtn');
    const closeAddBtn = document.getElementById('closeAddMemberModalBtn');

    if (addModal && openAddBtn && closeAddBtn) {
        openAddBtn.onclick = () => {
            addModal.style.display = 'flex';
        };
        closeAddBtn.onclick = () => {
            addModal.style.display = 'none';
        };
    }

    // 회원 수정 모달
    const editModal = document.getElementById('editMemberModal');
    const closeEditBtn = document.getElementById('closeEditMemberModalBtn');

    if (editModal && closeEditBtn) {
        closeEditBtn.onclick = () => {
            editModal.style.display = 'none';
        };
        window.onclick = (event) => {
            if (event.target === editModal) {
                editModal.style.display = 'none';
            }
        };
    }

    // 수정 모달 열기 함수
    window.openEditMemberModal = function(button) {
        const member = {
            id: button.getAttribute('data-id'),
            email: button.getAttribute('data-email'),
            role: button.getAttribute('data-role')
        };

        if (!editModal) return;

        editModal.style.display = 'flex';
        document.getElementById('editMemberId').value = member.id;
        document.getElementById('editMemberEmail').value = member.email;
        document.getElementById('editMemberRole').value = member.role;

        const form = document.getElementById('editMemberForm');
        form.action = `/api/admin/members/${member.id}`;
    };

    // 회원 삭제 모달
    const deleteModal = document.getElementById('deleteMemberModal');
    const cancelDeleteBtn = document.getElementById('cancelDeleteMemberBtn');
    const deleteForm = document.getElementById('deleteMemberForm');
    let deleteId = null;

    document.querySelectorAll('.deleteMemberBtn').forEach(button => {
        button.addEventListener('click', () => {
            deleteId = button.getAttribute('data-id');
            deleteForm.action = `/api/admin/members/${deleteId}`;
            deleteModal.style.display = 'flex';
        });
    });

    cancelDeleteBtn.addEventListener('click', () => {
        deleteModal.style.display = 'none';
        deleteId = null;
    });

    window.addEventListener('click', (event) => {
        if (event.target === deleteModal) {
            deleteModal.style.display = 'none';
            deleteId = null;
        }
    });

});
