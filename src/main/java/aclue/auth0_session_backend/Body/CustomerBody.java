package aclue.auth0_session_backend.Body;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomerBody {
    private String salutation;
    private String firstname;
    private String lastname;
    private String email;
    private String street;
    private String housingNumber;
    private String postalCode;
    private String city;
    private String additionalInformation;
}
