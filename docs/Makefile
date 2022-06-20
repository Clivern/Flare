help: Makefile
	@echo
	@echo " Choose a command run in docs:"
	@echo
	@sed -n 's/^##//p' $< | column -t -s ':' |  sed -e 's/^/ /'
	@echo


## config: Install required gems.
.PHONY: config
config:
	@echo ">> ============= Install required gems ============= <<"
	bundle install


## serve: Serve the docs.
.PHONY: serve
serve:
	@echo ">> ============= Serve the docs ============= <<"
	jekyll serve


## build: Build the docs.
.PHONY: build
build:
	@echo ">> ============= Build the docs ============= <<"
	jekyll build


.PHONY: help
