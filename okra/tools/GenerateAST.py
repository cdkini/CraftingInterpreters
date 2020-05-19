import sys


def define_type(base_name, class_name, fields, path):
    with open(path, "a") as f:
        f.write(f"  static class {class_name} extends {base_name} {\n")
        f.write(f"   {class_name}")


def define_AST(directory, base_name, types):
    path = f"{directory}/{base_name}.java"
    with open(path, "w") as f:
        f.write("package okra;\n")
        f.write("import java.util.List;\n")
        f.write(f"abstract class {base_name} {\n")
        f.write("}")

    for t in types:
        class_name, fields = t.split(":")
        define_type(base_name, class_name, fields, path)


def main():
    if len(sys.argv) != 2:
        print("Error: Please pass the directory you wish to output to")
        sys.exit(-1)
    directory = argv[1]
    define_AST(directory, "Expr", )

if __name__ == "__main__":
    main()
