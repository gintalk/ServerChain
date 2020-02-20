namespace java com.vng.zing.thrift.resource

// typedef i16 short;
// typedef i32 int;
// typedef string String;
// typedef bool boolean;
// typedef map<int, User> UserMap;

enum UserType{
    REGULAR = 0,
    PREMIUM = 1,
    ADMIN = 2
}

struct Token{
    1: optional string username;
    2: optional string password;
}

struct User{
    1: optional i32 id;
    2: optional string username;
    3: optional string name;
    4: optional UserType type;
    5: optional string joinDate;
}

struct TI32Result{
    1: required i32 error;
    2: optional i32 value;
    3: optional string extData;
}

struct TUserResult{
    1: required i32 error;
    2: optional User value;
    3: optional string extData;
}

// service Application{
//     TUserResult upgrade(1: User user);
// }

// service Account{
//     TI32Result add(1: Token token, 2: User user);
//     TI32Result remove(1: i32 uId);
// }

// service Authenticator{
//     TUserResult authenticate(1: string username, 2: string password);
// }

service TReadService{
    TUserResult authenticate(1: string username, 2: string password);
    TUserResult findById(1: i32 uId);
    TUserResult findByUsername(1: string username);
}

service TWriteService{
    TUserResult upgrade(1: User user);
    TI32Result add(1: Token token, 2: User user);
    TI32Result remove(1: i32 uId);
}
