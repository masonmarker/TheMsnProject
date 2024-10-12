class Var:
    # constructs a new Var
    def __init__(self, _msn2_reserved_varname, _msn2_reserved_varvalue):
        self.name = _msn2_reserved_varname
        self.value = _msn2_reserved_varvalue

    # determines equality of another Var
    def __eq__(self, other):
        if isinstance(other, Var):
            return other.name == self.name

    # string representation of Var
    def __str__(self):
        return f"(Var {self.name}={self.value})"