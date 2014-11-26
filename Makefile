#!/usr/bin/make

dev:
	@echo "Checking dev operations..."
	@if [ `which node` ]; then echo "nodejs... \033[32mOK\033[0m"; else echo "nodejs... \033[31mNOT ok\033[0m"; fi
	@if [ `which mocha` ]; then echo "mocha... \033[32mOK\033[0m"; else echo "mocha... \033[31mNOT ok\033[0m"; fi
	@node -p "var webdriver = require(\"selenium-webdriver\");\"\"";\
	if [ "$$?" -eq 0 ]; then echo "selenium-webdriver... \033[32mOK\033[0m"; else echo "selenium-webdriver for nodejs... \033[31mNOT ok\033[0m"; fi
	@node -p "var should = require(\"should\");\"\"";\
	if [ "$$?" -eq 0 ]; then echo "mocha should... \033[32mOK\033[0m"; else echo "mocha should... \033[31mNOT ok\033[0m"; fi

clean:
	rm captured_widgets/*.png

tests:
	@echo "testing something cool..."
	@mocha --require should tests/

.PHONY: dev tests clean
