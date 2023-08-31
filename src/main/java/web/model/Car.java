package web.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NamedNativeQueries(value = {
        @NamedNativeQuery(name = "Car.cleanTable", query = "TRUNCATE TABLE cars CASCADE"),
        @NamedNativeQuery(name = "Car.deleteTable", query = "DROP TABLE IF EXISTS cars CASCADE"),
        @NamedNativeQuery(name = "Car.createTable", query = "CREATE TABLE IF NOT EXISTS cars ("
                                                            + "id SERIAL PRIMARY KEY,"
                                                            + "brand VARCHAR(255) NOT NULL,"
                                                            + "series VARCHAR(255),"
                                                            + "model VARCHAR(255),"
                                                            + "color VARCHAR(255),"
                                                            + "user_id BIGINT REFERENCES users(id) ON DELETE CASCADE"
                                                            + ");"),

})

@Entity
@Table(name = "cars", schema = "public", catalog = "postgres")
@Getter
@Setter
@NoArgsConstructor
public class Car extends MyEntity {
    @Transient
    boolean toStringCalled;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonProperty("brand")
    private String brand;
    @JsonProperty("series")
    private String series;
    @JsonProperty("model")
    private String model;
    @JsonProperty("color")
    private String color;

    @JsonProperty("user")
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Car(String brand, String series, String model, String color) {
        this.brand = brand;
        this.series = series;
        this.model = model;
        this.color = color;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Car{");
        sb.append("id=").append(id);
        sb.append(", brand='").append(brand).append('\'');
        sb.append(", series='").append(series).append('\'');
        sb.append(", model='").append(model).append('\'');
        sb.append(", color=").append(color);
        if (toStringCalled) {
            toStringCalled = false;
        } else {
            if (user != null) user.toStringCalled = true;
            sb.append(", user=").append(user);
        }
        sb.append('}');
        return sb.toString();
    }
}
