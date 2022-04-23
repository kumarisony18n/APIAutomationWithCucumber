@Put
Feature: Check Put request for existing User details

  Scenario Outline: Put request to update existing User details with valid inputs
    Given LMS API is up and running for authorized user
    When User sends Put request with endpoint as "/Users/{id}" and from given sheetname "<SheetName>" and rownumber <RowNumber>
    Then User record is successfully updated with success status code

    Examples: 
      | SheetName | RowNumber |
      | Put    |         0 |
      | Put    |         1 |

 