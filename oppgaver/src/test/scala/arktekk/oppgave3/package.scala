package arktekk.oppgave3

import cats.parse.Parser
import org.scalatest.exceptions.TestPendingException

inline def implement_me[A]: Parser[A] = throw new TestPendingException
