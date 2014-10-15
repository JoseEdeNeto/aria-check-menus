#!/usr/bin/make

export PHANTOMJS_SET=`which phantomjs`
export CASPERJS_SET=`which casperjs`
export PHANTOMJS_FLAGS=""

dev:
	@echo "Checking dev operations..."
	@if [ "$(PHANTOMJS_SET)" ]; then echo "phantomjs... \033[32mOK\033[0m"; else echo "phantomjs... \033[31mNOT ok\033[0m"; fi
	@if [ "$(CASPERJS_SET)" ]; then echo "casperjs... \033[32mOK\033[0m"; else echo "casperjs... \033[31mNOT ok\033[0m"; fi

tests-unit:
	casperjs test tests/unit/* --fixtures=file://`pwd`/tests/fixture/

tests-functional:
	casperjs test tests/functional/* --fixtures=file://`pwd`/tests/fixture/

tests-all: tests-unit tests-functional

clean:
	rm captured_widgets/*.png

.PHONY: dev tests-unit tests-functional tests-all clean
