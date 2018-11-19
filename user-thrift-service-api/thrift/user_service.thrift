namespace java com.jianguo.thrift.user

struct UserDTO {
    1:i32 id,
    2:string username,
    3:string password,
    4:string realName,
    5:string mobile,
    6:string email
}

struct TeacherDTO {
    1:i32 id,
    2:string username,
    3:string password,
    4:string realName,
    5:string mobile,
    6:string email,
    7:string intro,
    8:string stars
}

service UserService{
    UserDTO getUserById(1:i32 id);

    UserDTO getUserByName(1:string username);

    void registerUser(1:UserDTO userDTO);

    TeacherDTO getTeacherById(1:i32 id);
}