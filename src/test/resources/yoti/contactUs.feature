Feature: yoti test

#  1. Go to https://www.yoti.com/
#  2. Click on the menu and open the menu
#  3. Go to Contact us (in the menu)
#  4. Expand “I have a question about my Yoti” section and submit the form
#  5. Verify the message displayed
#  6. Expand “I have a business question” section and submit the form
#  7. Verify the message displayed

  Scenario: I have a question about my Yoti
    Given I go to Yoti web site
    When I click on the menu
    Then I should be able to see Contact us in the opened menu
    When I click on Contact us
    Then I should be able to see following questions
      | I have a question about my Yoti |
      | I have a business question      |
    When I expand "I have a question about my Yoti" section
    And I submit the "my Yoti" form
    Then I should see the confirmation message for "my Yoti" form
    When I expand "I have a business question" section
    And I submit the "business" form
    Then I should see the confirmation message for "business" form
