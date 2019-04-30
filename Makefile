
VERSION=$(shell grep appVersion gradle.properties | cut -d '=' -f 2)
ARCHIVEDIR=as-rest-client-$(VERSION)
ARCHIVENAME=$(ARCHIVEDIR).tgz

.PHONY: package
package: clean war validatedocs
	mkdir $(ARCHIVEDIR)
	mkdir target
	cp build/libs/*.war $(ARCHIVEDIR)
	cp docs/swagger.json $(ARCHIVEDIR)
	tar -czvf target/$(ARCHIVENAME) $(ARCHIVEDIR)

.PHONY: war
war:
	./gradlew bootWar

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