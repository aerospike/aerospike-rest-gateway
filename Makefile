
VERSION=$(shell grep appVersion gradle.properties | cut -d '=' -f 2)
ARCHIVEDIR=aerospike-rest-gateway-$(VERSION)
ARCHIVENAME=$(ARCHIVEDIR).tgz

.PHONY: package
package: clean build validatedocs
	mkdir $(ARCHIVEDIR)
	mkdir target
	cp build/libs/*.jar $(ARCHIVEDIR)
	cp docs/openapi.json $(ARCHIVEDIR)
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
validatedocs:
	swagger-cli validate docs/openapi.json