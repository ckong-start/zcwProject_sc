package com.scw.user.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @create 2020-02-10 22:18
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserTest {

    private Integer id;
    private String username;
    private String password;
    private String email;

}
