
with open("/Users/jesseschmidt/Developer/aerospike-client-rest/src/main/java/com/aerospike/restclient/domain/operationmodels/HLLOperation.java") as f:
    d = {}
    line = f.readline()

    while line.strip():
        while ("class" not in line):
            line = f.readline()

        line = line.split()
#         print(line)
        class_ = line[1]

        while ("OperationTypes." not in line):
            line = f.readline()

        line = line.split()
        type_ = line[-1].replace(",", "")

#         print(type_)
#         print(line)
        print("{}.class,".format(class_))
#         print("@JsonSubTypes.Type(value = {}.class, name = {}),".format(class_, type_))
        line = f.readline()