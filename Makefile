
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
	cd target; sha256sum $(ARCHIVENAME) > $(ARCHIVENAME).sha256

.PHONY: build
build:
	./gradlew build -x test

.PHONY: image
image:
	docker build -t aerospike-rest-gateway .

.PHONY: run
run:
	./gradlew bootRun

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
