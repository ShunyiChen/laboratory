package com.shunyi.laboratory.xml.sax;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author esehhuc
 * @create 2021-03-16 16:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    int id;
    private String firstName;
    private String lastName;
    private String occupation;

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();
        builder.append("User{").append("id=").append(id)
                .append(", firstName=").append(firstName)
                .append(", lastName=").append(lastName)
                .append(", occupation=").append(occupation).append("}");

        return builder.toString();
    }
}
