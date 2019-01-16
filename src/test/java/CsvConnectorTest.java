import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 *
 */
public class CsvConnectorTest {



  private CsvParser csvParser;


  @Before
  public void setup() {
   this.csvParser = this.createCsvFileParser();
  }


  @Test
  public void testBeginAtOffset0() throws Exception {
    this.testBeginAtOffsetX(0, 10837);
  }

  @Test
  public void testBeginInMiddle() throws Exception {
    testBeginAtOffsetX(13000L, 9658);
  }





  public void testBeginAtOffsetX(long byteOffset, int expectedCount) throws Exception {

    List<String[]> results = new ArrayList<>();
    FileInputStream fis = new FileInputStream(Paths.get("src/test/resources/annual-enterprise-survey-2017-financial-year-provisional-size-bands-csv-utf-16.csv").toAbsolutePath().toString());

    fis.getChannel().position(byteOffset);

    this.csvParser.beginParsing(fis);
    // parse next once ...
    this.csvParser.parseNext();
    String[] nextRow = this.csvParser.parseNext();

    while(nextRow!=null) {

      for(String record:nextRow) {
        assertTrue(!record.contains("\u0000"));
      }
      results.add(nextRow);

      nextRow = this.csvParser.parseNext();

    }

    assertEquals(expectedCount-2, results.size());

  }

  public CsvParser createCsvFileParser() {
    CsvParserSettings parserSettings = new CsvParserSettings();
    parserSettings.setLineSeparatorDetectionEnabled(true);
    parserSettings.getFormat().setQuoteEscape('\\');
    parserSettings.setHeaderExtractionEnabled(false);
    parserSettings.getFormat().setCharToEscapeQuoteEscaping('\\');
    // creates a parser instance with the given settings
    return new CsvParser(parserSettings);
  }


}
