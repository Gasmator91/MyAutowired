package beans.person;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
@Qualifier("person_1")
public class PersonImpl_1 implements Person{
    private String name = "default";

    public PersonImpl_1() {
    }

    public PersonImpl_1(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
