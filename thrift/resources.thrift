namespace java com.vng.zing.resource.thrift

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
    2: optional string name;
    3: optional UserType type;
    4: optional string joinDate;
}

exception TZException{
    1: optional string message;
    2: optional string webMessage;
}

service Application{
    User upgrade(1: User user) throws(1: TZException ex);
}

service Account{
    void add(1: Token token, 2: User user) throws(1: TZException ex);
    void remove(1: i32 uId) throws(1: TZException ex);
}

service Authenticator{
    User authenticate(1: string username, 2: string password) throws(1: TZException ex);
}
