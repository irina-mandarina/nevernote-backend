package Entities;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
//@Table(name = "users")
public class User {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id", updatable = false)
    private Long id;

//    @Column(name = "name")
//    @NotNull
    private String username;


//    @Column(name = "password")
//    @NotNull
    private String password;

//    @Column(name = "name")
    private String name;

//    @Column(name = "name")
    private String address;

//    @Column(name = "age")
    private Integer age;

//    @Column(name = "bio")
    private String bio;
}
