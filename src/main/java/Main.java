import applicationContext.MyAnnotationConfigApplicationContext;
import configurations.MyConfiguration;
import beans.TestMyAutowired;
import org.springframework.context.support.AbstractApplicationContext;

public class Main {


    public static void main(String[] args) {

        AbstractApplicationContext context = new MyAnnotationConfigApplicationContext(MyConfiguration.class);
        TestMyAutowired test = ((TestMyAutowired)context.getBean("test"));
        TestMyAutowired test2 = context.getBean(TestMyAutowired.class);

        System.out.println(test + " - "
                + test.getPerson1() + " - "
                + test.getPerson2() + " - "
                + test.getPerson3() + " - "
                + test.getPerson4() + " - "
                + test.getMyComponent());


        System.out.println(test2 + " - "
                + test2.getPerson1() + " - "
                + test2.getPerson2() + " - "
                + test2.getPerson3() + " - "
                + test2.getPerson4() + " - "
                + test2.getMyComponent());

        context.registerShutdownHook();

    }
}
