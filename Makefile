unit:
	mvn test

functional:
	mvn integration-test

coverage:
	mvn cobertura:cobertura\;
	open target/site;

package:
	mvn assembly:single

help:
	@echo "****************************************************"
	@echo "*****      \033[1;34mJava Makefile help\033[0;0m                  *****"
	@echo "****************************************************"
	@echo ""
	@echo "    \033[32munit        :\033[0m    - run unit tests"
	@echo "    \033[32mfunctional  :\033[0m    - run functional tests"
	@echo "    \033[32mcoverage    :\033[0m    - run coverage report"
	@echo "    \033[32mpackage     :\033[0m    - generate single jar package"
	@echo "\n"

.PHONY: help coverage package functional unit
