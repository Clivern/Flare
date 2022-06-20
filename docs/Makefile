help: Makefile
	@echo
	@echo " Choose a command run in docs:"
	@echo
	@sed -n 's/^##//p' $< | column -t -s ':' |  sed -e 's/^/ /'
	@echo


.PHONY: config
config:
	@echo ">> ============= Install required gems ============= <<"
	bundle install


## serve: serve the docs.
.PHONY: serve
serve:
	@echo ">> ============= serve the docs ============= <<"
	jekyll serve


## build: build the docs.
.PHONY: build
build:
	@echo ">> ============= serve the docs ============= <<"
	jekyll build


.PHONY: help
