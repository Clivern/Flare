help: Makefile
	@echo
	@echo " Choose a command run in flare:"
	@echo
	@sed -n 's/^##//p' $< | column -t -s ':' |  sed -e 's/^/ /'
	@echo


.PHONY: test
test:
	@echo ">> ============= Install required gems ============= <<"
	./mvnw test


.PHONY: help
