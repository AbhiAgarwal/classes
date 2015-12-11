const x = {const f: 0, const g: true};

x.g ? x.f = 1 : x.f = 2; // type error

x.f
