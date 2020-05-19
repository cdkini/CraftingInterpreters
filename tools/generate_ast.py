import sys


def define_type(base_name, class_name, fields, path):
    """
    TODO: Fill in docstring
    """
    with open(path, "a") as f:
        f.write(f"    static class {class_name} extends {base_name} " + "{\n\n")
        f.write(f"        {class_name}({fields}) " + "{\n")
        
        fields = fields.split(", ")
        for field in fields:
            name = field.split()[1]
            f.write(f"            this.{name} = {name};" + "\n")
        f.write("    " + "}\n\n")
        
        for i, field in enumerate(fields):
            f.write(f"        final {field};" + "\n")
        f.write("  " + "}\n\n")


def define_AST(directory, base_name, types):
    """
    TODO: Fill in docstring
    """
    path = f"{directory}/{base_name}.java"
    with open(path, "w") as f:
        f.write("package src;" + "\n\n")
        f.write("import java.util.List;" + "\n\n")
        f.write(f"abstract class {base_name} " + "{\n\n")

    for t in types:
        class_name, fields = t.split(":")
        define_type(base_name, class_name.strip(), fields.strip(), path)


def main():
    if len(sys.argv) != 2:
        print("Error: Please pass the directory you wish to output to.")
        sys.exit(-1)
    directory = sys.argv[1]
    define_AST(directory, "Expression", 
        [
            "Binary: Expression left, Token operator, Expression right",
            "Grouping: Expression expression",
            "Literal: Object value",
            "Unary: Token operator, Expression right"
        ] 
    )


if __name__ == "__main__":
    main()
