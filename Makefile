
VERSION=$(shell git describe --tags)
ARCHIVEDIR=aerospike-rest-gateway-$(VERSION)
ARCHIVENAME=$(ARCHIVEDIR).tgz
OPENAPI_SPEC=build/openapi.json

.PHONY: package
package: clean validatedocs build
	mkdir $(ARCHIVEDIR)
	mkdir target
	cp build/libs/*.jar $(ARCHIVEDIR)
	cp $(OPENAPI_SPEC) $(ARCHIVEDIR)
	tar -czvf target/$(ARCHIVENAME) $(ARCHIVEDIR)

.PHONY: build
build:
	./gradlew build -x test

.PHONY: clean
clean:
	echo $(VERSION)
	rm -rf $(ARCHIVEDIR)
	rm  -f $(ARCHIVENAME)
	rm -rf target/
	./gradlew clean

.PHONY: validatedocs
validatedocs: $(OPENAPI_SPEC)
	swagger-cli validate $(OPENAPI_SPEC)

$(OPENAPI_SPEC):
	./gradlew clean generateApiDocs
