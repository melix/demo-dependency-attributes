## Dependency attributes

This test project demonstrates how dependency attributes can be used to select specific
components without having to explicitly declare an attribute on the resolved configuration.

## Usage

- `gradle help` : lists all versions of the modules with their dependencies
- `gradle` : resolves the dependencies, without any specific attribute

## Configuration

- `-Pall=release` : Adds an attribute on the resolved configuration to ask for components with status `release`
- `-PforceA=release`: Overrides whatever status was asked for the specific dependency `A` to `release`
- `-PforceB=release`: Overrides whatever status was asked for the specific dependency `B` to `release`
- `-Pquality=qa`: Adds an attribute on the resolved configuration to ask for components with quality `qa`



