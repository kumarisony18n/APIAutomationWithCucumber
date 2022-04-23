@Post
Feature: Check Post request for User API

  Scenario Outline: Post request to create new User
    Given LMS API is up and running for authorized user
    When User sends the Post request with valid inputs from given sheetname "<SheetName>" and rownumber <RowNumber> and endpoints as "/Users"
    Then New user record is successfully created with success status code

    Examples: 
      | SheetName | RowNumber |
      | Post    |         0 |
      | Post    |         1 |

 
