package models;

import com.avaje.ebean.Model;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Created by lubuntu on 8/20/16.
 */
@Entity
public class User extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    public String email;

    public String password;

    @OneToMany(mappedBy = "sender")
    public List<ConnectionRequest> connectionRequestsSent;

    @OneToMany(mappedBy = "receiver")
    public List<ConnectionRequest> connectionRequestsReceived;

    @OneToOne
    public Profile profile;

    @ManyToMany
    @JoinTable(name = "user_connections",
            joinColumns = {
                    @JoinColumn(name = "user_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "connection_id")
            }
    )

    public static User authenticate(String email, String password) {
        User user = User.find.where().eq("email", email).findUnique();
        if(user != null && BCrypt.checkpw(password, user.password)) {
            return user;
        }
        return null;
    }

    public Set<User> connections;

    public static Finder<Long, User> find = new Finder<Long, User>(User.class);

}