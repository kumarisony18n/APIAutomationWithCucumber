@Get
Feature: Check the Get request to fetch all skill data

  Scenario: Get request to fetch all skill data
    Given LMS API is up and running for authorized user
    When User sends Get request to fetch all skill data with endpoint as "/Skills"
    Then API should return all skill data with success status code

  Scenario Outline: Get request to fetch specific skill data
    Given LMS API is up and running for authorized user
    When User sends Get request to fetch specific skill data with endpoint as "/Skills/{id}" from given sheetname "<SheetName>" and rownumber <RowNumber>
    Then API should return record for specific skill with success status code

    Examples: 
      | SheetName | RowNumber |
      | Get       |         0 |
