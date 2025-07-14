package gift;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
public class ApplicationTest {

    @Autowired
    private ApplicationContext context; // 스프렝 부트에 대한 정보가 저장되어 있음

//    @DirtiesContext // 이걸 붙여주면 context 재활용을 피할 수 있게 됨, context를 매번 띄우게 됨
    @Test
    void test1() {
        System.out.println(context);
        System.out.println("test11");
        System.out.println(this);
    }
    @Test
    void test2() {
        System.out.println(context);
        System.out.println("test12");
        System.out.println(this);

    }

}
