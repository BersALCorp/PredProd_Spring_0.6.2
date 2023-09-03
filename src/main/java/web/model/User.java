package web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.io.IOException;

@NamedNativeQueries(value = {
        @NamedNativeQuery(name = "User.cleanTable", query = "TRUNCATE TABLE users CASCADE"),
        @NamedNativeQuery(name = "User.deleteTable", query = "DROP TABLE IF EXISTS users CASCADE"),
        @NamedNativeQuery(name = "User.createTable", query = "CREATE TABLE IF NOT EXISTS users ("
                + "id SERIAL PRIMARY KEY,"
                + "firstName VARCHAR(255) NOT NULL,"
                + "lastName VARCHAR(255) NOT NULL,"
                + "age INT NOT NULL,"
                + "address VARCHAR(255),"
                + "email VARCHAR(255) NOT NULL,"
                + "sex VARCHAR(50),"
                + "role VARCHAR(50),"
                + "car_id BIGINT REFERENCES cars(id) ON DELETE CASCADE"
                + ");"),
})

@Entity
@Table(name = "users", schema = "public", catalog = "postgres")
@Getter
@Setter
@NoArgsConstructor
public class User extends MyEntity {
    @Transient
    boolean toStringCalled;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("age")
    private int age;
    @JsonProperty("address")
    private String address;
    @JsonProperty("email")
    private String email;

    @Enumerated(EnumType.STRING)
    @JsonProperty("sex")
    @JsonDeserialize(using = SexEnumDeserializer.class)
    private SexEnum sex;

    @Enumerated(EnumType.STRING)
    @JsonProperty("role")
    @JsonDeserialize(using = RoleEnumDeserializer.class)
    private RoleEnum role;

    @JsonProperty("car")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "car_id")
    private Car car;

    public User(String firstName, String lastName, SexEnum sex, int age, String address, String email, RoleEnum role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.age = age;
        this.address = address;
        this.email = email;
        this.role = role;
    }

    public User(String firstName, String lastName, SexEnum sex, int age, String address, String email, RoleEnum role, Car car) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.age = age;
        this.address = address;
        this.email = email;
        this.role = role;
        this.car = car;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("id=").append(id);
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", sex='").append(sex).append('\'');
        sb.append(", age='").append(age).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", role='").append(role).append('\'');
        if (toStringCalled) {
            toStringCalled = false;
        } else {
            if (car != null) car.toStringCalled = true;
            sb.append(", car=").append(car);
        }
        sb.append('}');
        return sb.toString();
    }


    public static class SexEnumDeserializer extends JsonDeserializer<SexEnum> {
        @Override
        public SexEnum deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            String value = parser.getValueAsString();
            return (value != null) ? SexEnum.fromValue(value) : null;
        }
    }

    public static class RoleEnumDeserializer extends JsonDeserializer<RoleEnum> {
        @Override
        public RoleEnum deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            String value = jsonParser.getValueAsString();
            return (value != null) ? RoleEnum.fromValue(value) : null;
        }
    }
}

