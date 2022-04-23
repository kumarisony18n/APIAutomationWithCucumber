@Delete
Feature: Check Delete request to delete an existing user detail

  Scenario Outline: Delete request to delete existing user with valid input
    Given LMS API is up and running for authorized user
    When User sends the Delete request with valid input and endpoints as "/Users/{id}" from given sheetname "<SheetName>" and rownumber <RowNumber>
    Then user record is successfully deleted with success status code

    Examples: 
      | SheetName | RowNumber |
      | Delete  |         0 |


