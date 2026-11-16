package utb.fai.RESTAPIServer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<MyUser, Long> {

    @Query("SELECT u FROM MyUser u WHERE " +
           "(:name IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
           "(:phone IS NULL OR u.phoneNumber LIKE CONCAT('%', :phone, '%'))")
    List<MyUser> searchUsers(@Param("name") String name, 
                             @Param("email") String email, 
                             @Param("phone") String phone);
}