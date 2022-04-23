@Get
Feature: Check Get Request for User API

  Scenario: Get request to fetch all Users data
    Given LMS API is up and running for authorized user
    When User sends Get request with endpoint as "/Users"
    Then API should return all users data with success status code

  Scenario Outline: Get request to fetch specific User data
    Given LMS API is up and running for authorized user
    When User sends Get request to fetch specific user with endpoint as "/Users/{id}" from given sheetname "<SheetName>" and rownumber <RowNumber>
    Then API should return data for specific user with success status code
	Examples: 
      | SheetName | RowNumber |
      | Get  |         0 |