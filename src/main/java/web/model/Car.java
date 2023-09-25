package web.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

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

    public Car() {
    }

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


    public boolean isToStringCalled() {
        return toStringCalled;
    }

    public void setToStringCalled(boolean toStringCalled) {
        this.toStringCalled = toStringCalled;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
