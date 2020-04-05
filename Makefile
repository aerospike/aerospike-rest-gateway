
VERSION=$(shell grep appVersion gradle.properties | cut -d '=' -f 2)
ARCHIVEDIR=aerospike-client-rest-$(VERSION)
ARCHIVENAME=$(ARCHIVEDIR).tgz

.PHONY: package
package: clean build
	mkdir $(ARCHIVEDIR)
	mkdir target
	cp build/libs/*.jar $(ARCHIVEDIR)
	cp docs/swagger.json $(ARCHIVEDIR)
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
	ls -lat /root/.nvm/versions/node/v10.15.3/bin/swagger-tools
	env
	swagger-tools validate docs/swagger.json
	swagger-cli validate docs/swagger.json