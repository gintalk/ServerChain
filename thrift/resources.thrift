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

exception InvalidTokenException{
    1: string message;
}

exception DatabaseException{
    1: string message;
}

service App{
    User upgrade(1: User user) throws(1: DatabaseException dbe);
    string showInfo(1: User user) throws(1: DatabaseException dbe);
}

service Account{
    void add(1: Token token, 2: User user) throws(1: InvalidTokenException invalid);
    void remove(1: i32 uId) throws(1: InvalidTokenException invalid);
}

service Authenticator{
    User authenticate(1: string username, 2: string password) throws(1: InvalidTokenException invalid, 2: DatabaseException dbe);
}
