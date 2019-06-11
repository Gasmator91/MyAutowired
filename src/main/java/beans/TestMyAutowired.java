package beans;

import annotations.MyAutowired;
import beans.person.Person;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("test")
@Scope("prototype")
public class TestMyAutowired {

    @MyAutowired
    @Qualifier("person_1")
    private Person person1;

    @MyAutowired
    @Qualifier("person_2")
    private Person person2;

    private Person person3;
    private Person person4;


    private MyComponent myComponent;

    @MyAutowired
    private MyComponent myComponent1;

    public Person getPerson1() {
        return person1;
    }

    public Person getPerson2() {
        return person2;
    }

    @MyAutowired
    public void setPerson_3_4(@Qualifier("person_1") Person person3, @Qualifier("person_2") Person person4) {
        this.person3 = person3;
        this.person4 = person4;
    }

    public Person getPerson3() {
        return person3;
    }

    public Person getPerson4() {
        return person4;
    }

    @MyAutowired
    public void setMyComponent(MyComponent myComponent) {
        this.myComponent = myComponent;
    }

    public MyComponent getMyComponent() {
        return myComponent;
    }
}
