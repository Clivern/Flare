help: Makefile
	@echo
	@echo " Choose a command run in flare:"
	@echo
	@sed -n 's/^##//p' $< | column -t -s ':' |  sed -e 's/^/ /'
	@echo


## ci: Run the test cases
.PHONY: ci
ci:
	@echo ">> ============= Run test cases ============= <<"
	./mvnw test


.PHONY: help
