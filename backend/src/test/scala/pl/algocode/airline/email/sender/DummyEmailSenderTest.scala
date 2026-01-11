package pl.algocode.airline.email.sender

import pl.algocode.airline.email.EmailData
import pl.algocode.airline.test.BaseTest

class DummyEmailSenderTest extends BaseTest:
  it should "send scheduled email" in {
    DummyEmailSender(EmailData("test@sml.com", "subject", "content"))
    DummyEmailSender.findSentEmail("test@sml.com", "subject").isDefined shouldBe true
  }
