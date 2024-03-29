package ch.epfl.javions.adsb;

import ch.epfl.javions.Crc24;
import ch.epfl.javions.aircraft.IcaoAddress;
import ch.epfl.javions.demodulation.AdsbDemodulator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AirbornePositionMessageTest {


    private static final List<RawMessageData> EXPECTED_RAW_MESSAGE_DATA = List.of(
            new RawMessageData( 8096200L, "8D4B17E5F8210002004BB8B1F1AC" ),
            new RawMessageData( 75898000L, "8D49529958B302E6E15FA352306B" ),
            new RawMessageData( 100775400L, "8D39D300990CE72C70089058AD77" ),
            new RawMessageData( 116538700L, "8D4241A9601B32DA4367C4C3965E" ),
            new RawMessageData( 129268900L, "8D4B1A00EA0DC89E8F7C0857D5F5" ),
            new RawMessageData( 138560100L, "8D4D222860B985F7F53FAB33CE76" ),
            new RawMessageData( 146689300L, "8D440237990D6D9FB8088CC99EC4" ),
            new RawMessageData( 208135700L, "8D4D029F594B52EFDB7E94ACEAC8" ),
            new RawMessageData( 208341000L, "8D4D029F9914E09BB8240567C1D6" ),
            new RawMessageData( 210521800L, "8F01024C99256F1F78048C290D2D" ),
            new RawMessageData( 232125000L, "8D4B17E5990CB61068400CDD09D9" ),
            new RawMessageData( 233069800L, "8D3C648158AF92F723BC275EC692" ),
            new RawMessageData( 235839800L, "8D4952999915769CF02089DB69B1" ),
            new RawMessageData( 270464000L, "8D4D22289909451F10048C38343B" ),
            new RawMessageData( 285706800L, "8D4B2964EA234860015C00864171" ),
            new RawMessageData( 288802400L, "8D4B1900990DC48E380485203866" ),
            new RawMessageData( 316898700L, "8D4241A999086D0DD0840CDBACA6" ),
            new RawMessageData( 349256100L, "8D4B1A0058337639355B77835CBF" ),
            new RawMessageData( 349526700L, "8D4B1A00990CD99608480A2FD6D9" ),
            new RawMessageData( 373294000L, "8D4241A9EA11A898011C08B21C01" ),
            new RawMessageData( 379261900L, "8D4B1A00F8210002004BB8B7E02D" ),
            new RawMessageData( 385821900L, "8D39D300EA4A5867A53C089D5A75" ),
            new RawMessageData( 408163200L, "8D3C64819908E62EF0048B0C93B1" ),
            new RawMessageData( 429622400L, "8D4BCDE99915851930048B22CFAA" ),
            new RawMessageData( 445705200L, "8D4D029FF8132006005AB8D115B9" ),
            new RawMessageData( 470281500L, "8D4D029FEA1978667B5F042202CE" ),
            new RawMessageData( 493403000L, "8D495299EA447860015F8829B831" ),
            new RawMessageData( 500806100L, "8D39D300F82100020049B851B34D" ),
            new RawMessageData( 559063400L, "8D4B1900EA3E9860013C0897BF27" ),
            new RawMessageData( 587873700L, "8D4D222860B9827A1547B81799F2" ),
            new RawMessageData( 600759300L, "8D39D30058C382773D3FA0FB47B8" ),
            new RawMessageData( 612521000L, "8D4BCDE958B98681617391A02830" ),
            new RawMessageData( 633078600L, "8D3C648158AF92F741BC3075DA97" ),
            new RawMessageData( 636533100L, "8D4B29649910770C105008F4F234" ),
            new RawMessageData( 645590600L, "8D4D029F594B52EFC97E8B9546CA" ),
            new RawMessageData( 645795900L, "8D4D029F9914E09BB8240567C1D6" ),
            new RawMessageData( 705844800L, "8D4952999915769CF02089DB69B1" ),
            new RawMessageData( 715212600L, "8D4D0221581362C8E95F06DFFD44" ),
            new RawMessageData( 773099400L, "8D3C6481EA42885C573C08357403" ),
            new RawMessageData( 797810600L, "8D4CA2BF606502FF0121D5D16090" ),
            new RawMessageData( 812236600L, "8D4951CE60738329CBB3E7A16B8B" ),
            new RawMessageData( 833094200L, "8D3C64819908E62EF0048B0C93B1" ),
            new RawMessageData( 857674200L, "8D39CEAA58AF865FDEF935606271" ),
            new RawMessageData( 858094400L, "8D39CEAA99144D36000489F83CF3" ),
            new RawMessageData( 868432200L, "8D495299E11B1100000000DDB0DF" ),
            new RawMessageData( 1008567300L, "8D49529958B302E6B95F836AEF91" ),
            new RawMessageData( 1032331100L, "8D4B17E5582522AD9155733599CF" ),
            new RawMessageData( 1063803000L, "8D495299F8230002004AB8F789C5" ),
            new RawMessageData( 1065855100L, "8DA4F2399915C318B0048AC5BDBE" ),
            new RawMessageData( 1127252300L, "8D4D222860B985F8273FCA1B84EA" ),
            new RawMessageData( 1138542200L, "8D4241A9601B4656B55F082C95CF" ),
            new RawMessageData( 1149118600L, "8D394C13E10E8B000000001CB2F5" ),
            new RawMessageData( 1164431700L, "8D4D22289909451F10088C706E3B" ),
            new RawMessageData( 1184750000L, "8D4D029F594B466BB3752CFB160C" ),
            new RawMessageData( 1184955200L, "8D4D029F9914E09BB8240567C1D6" ),
            new RawMessageData( 1214082000L, "8D394C1360B505E402E63112F294" ),
            new RawMessageData( 1218171500L, "8D4B29649910770C105008F4F234" ),
            new RawMessageData( 1219038000L, "8D394C139908E1AFD8088A824A12" ),
            new RawMessageData( 1223069500L, "8D3C648158AF92F76FBC3E72A971" ),
            new RawMessageData( 1224093900L, "8D394C13EA447858013C08073C80" ),
            new RawMessageData( 1233094200L, "8D3C64819908E62EF0048B0C93B1" ),
            new RawMessageData( 1244978500L, "8D4D022158134645A1567E0FF5EB" ),
            new RawMessageData( 1280289300L, "8D4B17E5990CB21108400DCAAFC0" ),
            new RawMessageData( 1288753400L, "8D4952999915769CF02089DB69B1" ),
            new RawMessageData( 1323470700L, "8D4BCDE9EA466867497C08E62323" ),
            new RawMessageData( 1350181200L, "8D4CA2BFEA291866151C08123192" ),
            new RawMessageData( 1399281900L, "8D4B1A00583362BC33640F90AD74" ),
            new RawMessageData( 1399822900L, "8D4B1A00EA0DC89E8F7C0857D5F5" ),
            new RawMessageData( 1422737500L, "8D4BCDE99915851930048B22CFAA" ),
            new RawMessageData( 1437501700L, "8D39CEAA58AF866012F9311516E8" ),
            new RawMessageData( 1437921600L, "8D39CEAA99144D36000489F83CF3" ),
            new RawMessageData( 1447383100L, "8D4D022199108D1138540D133A2B" ),
            new RawMessageData( 1472930400L, "8D4241A999086D0DD0840B247C82" ),
            new RawMessageData( 1499146900L, "8D4D2228234994B7284820323B81" ),
            new RawMessageData( 1515918300L, "8D39D30058C385F57F37C1B8861A" ),
            new RawMessageData( 1518447400L, "8D4D2228EA466864931C082073D1" ),
            new RawMessageData( 1541942000L, "8D4CA2BF990C4FB03018085E8C8D" ),
            new RawMessageData( 1580887900L, "8D4D222860B9827A4747D7A66FAA" ),
            new RawMessageData( 1581106900L, "8D4BCDE958B98305E57CB5879942" ),
            new RawMessageData( 1605989900L, "8D39D300990CE72C70049010F777" ),
            new RawMessageData( 1645019700L, "8D4D029F594B42EF9D7E75C0F7E5" ),
            new RawMessageData( 1646217000L, "8D4241A9F82300030048B81F5D33" ),
            new RawMessageData( 1648348000L, "8D4B17E5EA0BD89C1D7C0824B27A" ),
            new RawMessageData( 1656866000L, "8D44023758BF06021F11007456B4" ),
            new RawMessageData( 1687618800L, "8D4D029FEA1978667B5F042202CE" ),
            new RawMessageData( 1688150900L, "8D3C648158AF92F78DBC47B1A748" ),
            new RawMessageData( 1689976100L, "8D4D0221EA0DC898015E182AAE02" ),
            new RawMessageData( 1714855000L, "8D4CA2BF606502FEB921CE197BF9" ),
            new RawMessageData( 1722323400L, "8D4951CE99090B1D980808F84DB7" ),
            new RawMessageData( 1731065400L, "8D4952999915769CF02089DB69B1" ),
            new RawMessageData( 1761704200L, "8D440237EA485858013C08109653" ),
            new RawMessageData( 1762693600L, "8D4951CE607386A501A95EC0DE7F" ),
            new RawMessageData( 1773870400L, "8D495299EA447860015F8829B831" ),
            new RawMessageData( 1823092600L, "8D3C64819908E62EF8048B6231B9" ),
            new RawMessageData( 1835102700L, "8D4D0221581342C90B5F16AB915B" ),
            new RawMessageData( 1859535300L, "8D4B1A00990CD895E8480A755B34" ),
            new RawMessageData( 1872922900L, "8D39CEAA99144D36000489F83CF3" ),
            new RawMessageData( 1896411000L, "8D4B17E5582512ADA75565D069A5" ),
            new RawMessageData( 1905870900L, "8DA4F2399915C318B8048AAB1FB6" ),
            new RawMessageData( 1930690100L, "8D4241A9E1059700000000B7C67C" ),
            new RawMessageData( 1955871700L, "8DA4F23958AF8567092906082F82" ),
            new RawMessageData( 1993098600L, "8D3C6481EA42885C573C08357403" ),
            new RawMessageData( 2011173700L, "8D49529958B30662B356CC3FFCA0" ),
            new RawMessageData( 2012822900L, "8D4D022199108D1138540D133A2B" ),
            new RawMessageData( 2031706700L, "8D440237990D6D9FB8048C81C4C4" ),
            new RawMessageData( 2075921200L, "8D4D029F594B42EF8B7E6CCE2ECE" ),
            new RawMessageData( 2076126500L, "8D4D029F9914E09BB8240567C1D6" ),
            new RawMessageData( 2076864400L, "8D4241A9601B62DA6D67D836061B" ),
            new RawMessageData( 2118341200L, "8D4D222860B985F8573FE9F76D23" ),
            new RawMessageData( 2175222800L, "8D4D2228F823000600487886B825" ),
            new RawMessageData( 2176455400L, "8D4B17E5990CAD11A8440D7C2FBE" ),
            new RawMessageData( 2181294100L, "8D4D22289909451F10088C706E3B" ),
            new RawMessageData( 2186271000L, "8D4B29645949A68E578D05384ACA" ),
            new RawMessageData( 2186476200L, "8D4B29649910770C105008F4F234" ),
            new RawMessageData( 2240535600L, "8F01024C233530F3CF6C60A19669" ),
            new RawMessageData( 2266325300L, "8D4952999915769CF02089DB69B1" ),
            new RawMessageData( 2283115100L, "8D3C648158AF92F7BBBC55059A34" ),
            new RawMessageData( 2288212800L, "8D3C64819908E62EF8048B6231B9" ),
            new RawMessageData( 2289331500L, "8D4B1A00583362BC1563FE29589A" ),
            new RawMessageData( 2289602000L, "8D4B1A00990CD895E8480A755B34" ),
            new RawMessageData( 2345132300L, "8D4D0221581342C9155F1C34E2B2" ),
            new RawMessageData( 2367505900L, "8D39CEAA58AF82E42CFF654A58C2" ),
            new RawMessageData( 2367925700L, "8D39CEAA99144D36000489F83CF3" ),
            new RawMessageData( 2386931400L, "8D4BCDE99915851938048B4C6DA2" ),
            new RawMessageData( 2471212800L, "8D49529958B302E6715F4A9FB0A3" ),
            new RawMessageData( 2473121200L, "8D3C6481F82300020049B832939F" ),
            new RawMessageData( 2496301600L, "8D4CA2BF990C4AB0501408D67300" ),
            new RawMessageData( 2516450900L, "8D4241A999086D0DD0840B247C82" ),
            new RawMessageData( 2558471700L, "8D4B17E19908EFA5A8A00C7AA410" ),
            new RawMessageData( 2567937000L, "8D4D222860B9827A7947F77DCDAD" ),
            new RawMessageData( 2580798000L, "8D39D30058C38277CB3F73D4F792" ),
            new RawMessageData( 2584585300L, "8D4BCDE958B98681AF734786CE14" ),
            new RawMessageData( 2597642100L, "8D4D022199108C1118580D1D16CC" ),
            new RawMessageData( 2619259200L, "8D4B1A00EA0DC89E8F7C0857D5F5" ),
            new RawMessageData( 2662471400L, "8D4D029F594B42EF6F7E5F223666" ),
            new RawMessageData( 2662676700L, "8D4D029F9914E09BB8240567C1D6" ),
            new RawMessageData( 2693625500L, "8D4952999915769CF02089DB69B1" ),
            new RawMessageData( 2698727800L, "8D49529923501439CF1820419C55" ),
            new RawMessageData( 2707170900L, "8D4D22289909451F10088C706E3B" ),
            new RawMessageData( 2729261700L, "8D4B1A0058336638E35B45087116" ),
            new RawMessageData( 2729532100L, "8D4B1A00990CD895E8480A755B34" ),
            new RawMessageData( 2789833600L, "8D4D2228EA466864931C082073D1" ),
            new RawMessageData( 2799304900L, "8D4B1A00F8210002004BB8B7E02D" ),
            new RawMessageData( 2804904800L, "8D4241A9EA11A898011C08B21C01" ),
            new RawMessageData( 2808517000L, "8D4B17E55825162ADD4D0404FD7B" ),
            new RawMessageData( 2813102200L, "8D3C648158AF9673C5B18C4AEC5F" ),
            new RawMessageData( 2870825200L, "8D39D300EA4A5867A53C089D5A75" ),
            new RawMessageData( 2883205900L, "8D3C64819908E62EF8048B6231B9" ),
            new RawMessageData( 2896592600L, "8D4B17E5EA0BD89C1D7C0824B27A" ),
            new RawMessageData( 2900188000L, "8D4D029FEA1978667B5F042202CE" ),
            new RawMessageData( 2906194400L, "8D49529958B302E65F5F3B65C105" ),
            new RawMessageData( 2911496600L, "8D4D022158134645CB5694B0537D" ),
            new RawMessageData( 2912510900L, "8D39CEAA58AF86608AF926B3C4A1" ),
            new RawMessageData( 2912934400L, "8D39CEAA99144D36000489F83CF3" ),
            new RawMessageData( 2935911000L, "8DA4F23958AF85672F28DA6B8A8F" ),
            new RawMessageData( 3011236500L, "8D495299EA447860015F8829B831" ),
            new RawMessageData( 3066745000L, "8D440237990D6D9FB8048C81C4C4" ),
            new RawMessageData( 3113140100L, "8D4D022199108C11185C0D2520CC" ),
            new RawMessageData( 3128442900L, "8D4B17E1F8210002004BB8B56D2D" ),
            new RawMessageData( 3148679700L, "8D4952999915779CF02089D8637F" ),
            new RawMessageData( 3150286900L, "8D4D222860B985F8894009F592C9" ),
            new RawMessageData( 3163623100L, "8D3C6545EA4A5858013C0839B8AE" ),
            new RawMessageData( 3171165200L, "8D4B29649910770C104C085C7034" ),
            new RawMessageData( 3179279100L, "8D4B1A0058335638D55B3DEF97ED" ),
            new RawMessageData( 3179549600L, "8D4B1A00990CD895E8440A3D0134" ),
            new RawMessageData( 3210517700L, "8F01024C99256F1F78048C290D2D" ),
            new RawMessageData( 3215880100L, "8DA4F23925101331D73820FC8E9F" ),
            new RawMessageData( 3303948100L, "8D4D22289909451F10088C706E3B" ),
            new RawMessageData( 3322516000L, "8D39CEAA58AF82E484FF5D8AD34A" ),
            new RawMessageData( 3322936000L, "8D39CEAA99144D36000489F83CF3" ),
            new RawMessageData( 3328440000L, "8D4B17E1E11AAC000000004DD627" ),
            new RawMessageData( 3333067700L, "8D3C648158AF92F80DBC6ED60A18" ),
            new RawMessageData( 3358467600L, "8D4B17E15857130F674183178291" ),
            new RawMessageData( 3380509700L, "8F01024CF8330006004AB8250493" ),
            new RawMessageData( 3413090900L, "8D3C64819908E62EF8048B6231B9" ),
            new RawMessageData( 3424260600L, "8D4BCDE9F8230006004BB8A0027C" ),
            new RawMessageData( 3428806700L, "8D49529958B312E6495F29A42CB0" ),
            new RawMessageData( 3475900700L, "8DA4F239F8330006004AB8123C53" ),
            new RawMessageData( 3486858100L, "8D4BCDE99915851938048B4C6DA2" ),
            new RawMessageData( 3495274700L, "8D4D0221581332C9375F2CF851D1" ),
            new RawMessageData( 3518653400L, "8D495299F8230002004AB8F789C5" ),
            new RawMessageData( 3532024100L, "8D4241A999086D0DD07C0B0F14BD" ),
            new RawMessageData( 3555524300L, "8D4D0221F8230002004AB8A3C16F" ),
            new RawMessageData( 3577785100L, "8D4D022199108C10F85C0D2B5D49" ),
            new RawMessageData( 3588444200L, "8D4B17E19908EEA5A8A00C79AEDE" ),
            new RawMessageData( 3663721900L, "8D44023758BF0601BB10BA983E9E" ),
            new RawMessageData( 3682884300L, "8D4D222860B9827AAF481A4C3807" ),
            new RawMessageData( 3708360900L, "8D4B29645949C68E758D17C588E2" ),
            new RawMessageData( 3708566200L, "8D4B29649910770C105008F4F234" ),
            new RawMessageData( 3719244300L, "8D4B1A0058335638BF5B30AAA54F" ),
            new RawMessageData( 3719514700L, "8D4B1A00990CD795C8440A691F07" ),
            new RawMessageData( 3727943400L, "8D39CEAA99144D36000489F83CF3" ),
            new RawMessageData( 3732622800L, "8D4951CE6073832A55B435862EAE" ),
            new RawMessageData( 3756122600L, "8D4CA2BF606512FE1B21C162A757" ),
            new RawMessageData( 3758671100L, "8D4D22289909451F10088C706E3B" ),
            new RawMessageData( 3768086500L, "8D3C648158AF92F82BBC7740EB0A" ),
            new RawMessageData( 3790657900L, "8D4BCDE9EA466867497C08E62323" ),
            new RawMessageData( 3843907900L, "8D49529958B312E6355F1A11D6AD" ),
            new RawMessageData( 3860772600L, "8D4CA2BFEA2D0866151C0809ED28" ),
            new RawMessageData( 3885921800L, "8DA4F23958AF81E6E92FEDD50D36" ),
            new RawMessageData( 3909284000L, "8D4B1A00EA0DC89E8F7C0857D5F5" ),
            new RawMessageData( 3933091000L, "8D3C64819908E62EF8048B6231B9" ),
            new RawMessageData( 3986892300L, "8D4B2964EA234860015C00864171" ),
            new RawMessageData( 4011575200L, "8D4D022158133645EB56A467489C" ),
            new RawMessageData( 4011725100L, "8D440237F82300030049B823E8E4" ),
            new RawMessageData( 4038999700L, "8D4D2228EA466864931C082073D1" ),
            new RawMessageData( 4077709100L, "8D4D022199108C10F85C0D2B5D49" ),
            new RawMessageData( 4103219900L, "8D4B2964212024123E0820939C6F" ),
            new RawMessageData( 4144742000L, "8D4B17E5EA0BD89C1D7C0824B27A" ),
            new RawMessageData( 4171624600L, "8D4CA2BF990C41B07014088E460B" ),
            new RawMessageData( 4173289500L, "8D4D029F9914DF9BB824052756EC" ),
            new RawMessageData( 4175518300L, "8D4D0221EA0DC898015E182AAE02" ),
            new RawMessageData( 4176740300L, "8D4B17E5990C9F1348440D59B572" ),
            new RawMessageData( 4200509200L, "8D01024C99256F1F78048C99EFDD" ),
            new RawMessageData( 4201726800L, "8D440237EA485858013C08109653" ),
            new RawMessageData( 4211726900L, "8D440237990D6D9FB8088CC99EC4" ),
            new RawMessageData( 4213719800L, "8D4952999915779CF02089D8637F" ),
            new RawMessageData( 4220520200L, "8D01024C58B99639199DC1080441" ),
            new RawMessageData( 4222114100L, "8D4D222860B985F8BF402B408EDD" ),
            new RawMessageData( 4222824200L, "8D4B29645949C31313970B3F89A9" ),
            new RawMessageData( 4223029400L, "8D4B29649910770C104C085C7034" ),
            new RawMessageData( 4227579400L, "8D39CEAA58AF866102F91C1DD248" ),
            new RawMessageData( 4228002900L, "8D39CEAA99144E36000489FD23A1" ),
            new RawMessageData( 4229259700L, "8D4B1A00583352BBCF63D46240A1" ),
            new RawMessageData( 4229530200L, "8D4B1A00990CD795C8440A691F07" ),
            new RawMessageData( 4261388900L, "8D4CAC87991506AD58088C7FB79B" ),
            new RawMessageData( 4294540900L, "8D4D22289909451F10088C706E3B" ),
            new RawMessageData( 4368110200L, "8D3C64819908E62EF8048B6231B9" ),
            new RawMessageData( 4368912000L, "8D49529958B31662475676C68E5D" ),
            new RawMessageData( 4421279500L, "8D4BCDE958B98681F97301FE2C9D" ),
            new RawMessageData( 4424072900L, "8D3C654558C396BA8BB794BB4351" ),
            new RawMessageData( 4452202800L, "8D4B2964F8132006005AB8E3A7B3" ),
            new RawMessageData( 4475329800L, "8D4D022158132645F756A92CEF33" ),
            new RawMessageData( 4478263300L, "8D3C6481EA42885C573C08357403" ),
            new RawMessageData( 4483266200L, "8D3C6481E10D0100000000A919FA" ),
            new RawMessageData( 4535927800L, "8DA4F2399915C318B8048AAB1FB6" ),
            new RawMessageData( 4568559900L, "8D3C65459908CD30780490EA95F8" ),
            new RawMessageData( 4570410500L, "8D4BCDE99915851938048B4C6DA2" ),
            new RawMessageData( 4581047700L, "8D4D029F594B366B1F74E2940745" ),
            new RawMessageData( 4581253000L, "8D4D029F9914DF9BB824052756EC" ),
            new RawMessageData( 4582695900L, "8D4D022199108B10D85C0D67150A" ),
            new RawMessageData( 4590456200L, "8D4D2228F823000600487886B825" ),
            new RawMessageData( 4608801200L, "8D4B17E55823F62B174CE929B2A0" ),
            new RawMessageData( 4619251500L, "8D4B1A00224C16B5D2082077AB1A" ),
            new RawMessageData( 4623269100L, "8D4CA2BF990C41B07014088E460B" ),
            new RawMessageData( 4633873700L, "8D4952999915779CF01C894EA576" ),
            new RawMessageData( 4669266500L, "8D4B1A00583342BBC163CBEFF75F" ),
            new RawMessageData( 4669537000L, "8D4B1A00990CD795C8480A214507" ),
            new RawMessageData( 4672806400L, "8D4951CE6073832A81B44F0E093A" ),
            new RawMessageData( 4673064100L, "8D3C648158AF96744BB1B43010C1" ),
            new RawMessageData( 4676666100L, "8D4B2964E115BC000000008AC818" ),
            new RawMessageData( 4722539900L, "8D39CEAA58AF82E4FEFF5212B9E3" ),
            new RawMessageData( 4722963600L, "8D39CEAA99144E36000489FD23A1" ),
            new RawMessageData( 4730733600L, "8D4B29645949C68E878D2327FDF8" ),
            new RawMessageData( 4730938900L, "8D4B29649910770C104C085C7034" ),
            new RawMessageData( 4750361000L, "8D4D0221224D65F2E56360ADAA1E" ),
            new RawMessageData( 4768552300L, "8D4D222860B985F8D9403C5CC3DC" ),
            new RawMessageData( 4825922400L, "8DA4F23958AF81E70D2FC5ED906B" ),
            new RawMessageData( 4963119500L, "8D3C6481F82300020049B832939F" ),
            new RawMessageData( 5005920500L, "8DA4F239EA428864015C08A3C151" ),
            new RawMessageData( 5021779700L, "8D4D029F594B32EF057E29097812" ),
            new RawMessageData( 5021984900L, "8D4D029F9914DF9BB824052756EC" ),
            new RawMessageData( 5109255900L, "8D4B1A00EA0DC89E8F7C0857D5F5" ),
            new RawMessageData( 5123076500L, "8D3C648158AF967469B1BD91400F" ),
            new RawMessageData( 5127926700L, "8D4D022199108B10B8600D3E5778" ),
            new RawMessageData( 5159521200L, "8D4B1A00990CD795C8480A214507" ),
            new RawMessageData( 5219108600L, "8D4D222860B985F8EF404AEA8446" ),
            new RawMessageData( 5232903200L, "8D4B17E5990C971408440D18E9E9" ),
            new RawMessageData( 5240059000L, "8D4CA2BF234994B5E70DE0BAE6F8" ),
            new RawMessageData( 5250520700L, "8D01024C58B992BC71A7F3935208" ),
            new RawMessageData( 5259942500L, "8D4B29645949D313279717A0E4F6" ),
            new RawMessageData( 5260147800L, "8D4B29649910770C104808644634" ),
            new RawMessageData( 5269207800L, "8D4D2228EA466864931C082073D1" ),
            new RawMessageData( 5277535800L, "8D39CEAA58AF866158F915C0E0E5" ),
            new RawMessageData( 5279604000L, "8D4B2964EA234860015C00864171" ),
            new RawMessageData( 5316792000L, "8D440237990D6D9FB8088CC99EC4" ),
            new RawMessageData( 5349263100L, "8D4B1A00F8210002004BB8B7E02D" ),
            new RawMessageData( 5374073500L, "8D3C654558C3934025C2ECF94AC1" ),
            new RawMessageData( 5403529100L, "8D4D029FEA1978667B5F042202CE" ),
            new RawMessageData( 5416696100L, "8D4D22289909451F10048DC7C032" ),
            new RawMessageData( 5418420800L, "8D4B17E15855D68A6339DBD68C70" ),
            new RawMessageData( 5422783600L, "8D4D0221EA0DC898015E182AAE02" ),
            new RawMessageData( 5444719600L, "8D4BB2795869A29ADD93D0A7BA00" ),
            new RawMessageData( 5472936100L, "8D4B17E55823F2AE1B55309CBF71" ),
            new RawMessageData( 5480739200L, "8D4D029F9914E09BB8240567C1D6" ),
            new RawMessageData( 5494001400L, "8D495299EA447860015F8829B831" ),
            new RawMessageData( 5526691500L, "8D4CA2BF990C3CB09014082FF7FB" ),
            new RawMessageData( 5529686500L, "8D4D029FF8132006005AB8D115B9" ),
            new RawMessageData( 5544778400L, "8D4BB279990D4E17B8440735BD6E" ),
            new RawMessageData( 5607405000L, "8D4D022199108B10B8600D3E5778" ),
            new RawMessageData( 5619249800L, "8D4B1A00583342BB9F63B6070EC8" ),
            new RawMessageData( 5619520400L, "8D4B1A00990CD795C8440A691F07" ),
            new RawMessageData( 5648568500L, "8D3C6545EA4A5858013C0839B8AE" ),
            new RawMessageData( 5684907400L, "8D4CA2BF606512FD8321B545D829" ),
            new RawMessageData( 5688356200L, "8D3C648158AF967495B1CACD05E9" ),
            new RawMessageData( 5693093200L, "8D3C6481EA42885C573C08357403" ),
            new RawMessageData( 5705933900L, "8DA4F2399915C318B8048AAB1FB6" ),
            new RawMessageData( 5710204500L, "8D4CA2BFE102BF00000000A22DDD" ),
            new RawMessageData( 5715422700L, "8D4B29645949D68E9B8D2F93AE5E" ),
            new RawMessageData( 5715627900L, "8D4B29649910780C10480875242E" ),
            new RawMessageData( 5722728800L, "8D4951CE99090B1D980408B017B7" ),
            new RawMessageData( 5723088700L, "8D3C64819908E62EF8048B6231B9" ),
            new RawMessageData( 5736587400L, "8D49529958B312E5DB5ED200F24C" ),
            new RawMessageData( 5742208300L, "8D4D222860B9827B17485C78FC85" ),
            new RawMessageData( 5765929900L, "8DA4F23958AF85679D2860045546" ),
            new RawMessageData( 5847966700L, "8D39CEAA99144E36000489FD23A1" ),
            new RawMessageData( 5947403200L, "8D4D22289909451F10048C38343B" ),
            new RawMessageData( 5998271000L, "8D4D029F594B32EED97E13C783BD" ),
            new RawMessageData( 5998476400L, "8D4D029F9914E09BB8240567C1D6" ),
            new RawMessageData( 6025450100L, "8D4D0221581316461F56BEB50FC4" ),
            new RawMessageData( 6049248700L, "8D4B1A00583332BB9163AD296B40" ),
            new RawMessageData( 6060510700L, "8F01024CEA466864015C0866F082" ),
            new RawMessageData( 6103072700L, "8D3C648158AF8674BBB1D505CE3E" ),
            new RawMessageData( 6117960100L, "8D4CA2BF990C37B09018087AE4D9" ),
            new RawMessageData( 6163110900L, "8D4D022199108A10B8600CC2A9BF" ),
            new RawMessageData( 6242739900L, "8D4CA2BF606512FD5921B38D4E61" ),
            new RawMessageData( 6248882000L, "8D4BCDE9EA466867497C08E62323" ),
            new RawMessageData( 6252823600L, "8D4B29645949E3133B97228B4B9D" ),
            new RawMessageData( 6286746200L, "8D440237990D6D9FB8088CC99EC4" ),
            new RawMessageData( 6288172900L, "8D3C64819908E62EF8048B6231B9" ),
            new RawMessageData( 6297064200L, "8D4B17E5990C8F14E8400D2DA5AF" ),
            new RawMessageData( 6310512200L, "8F01024C58B99639819D7618F388" ),
            new RawMessageData( 6316314200L, "8D4BCDE958B986824572B9A1BEB4" ),
            new RawMessageData( 6336628600L, "8D4B1A1EEA3CA865733C082A79CD" ),
            new RawMessageData( 6339239700L, "8D4B1A00EA0DC89E8F7C0857D5F5" ),
            new RawMessageData( 6375464700L, "8D4CA2BFEA2D0866151C0809ED28" ),
            new RawMessageData( 6380505800L, "8F01024C99256F1F58048C6C7104" ),
            new RawMessageData( 6408458600L, "8D4B17E15855C68A2739F2A5CC13" ),
            new RawMessageData( 6432548700L, "8D39CEAA58AF8661BEF90C36FD08" ),
            new RawMessageData( 6432972300L, "8D39CEAA99144E36000489FD23A1" ),
            new RawMessageData( 6495446500L, "8D4D2228EA466864931C082073D1" ),
            new RawMessageData( 6503498300L, "8D4D0221581306462B56C4280466" ),
            new RawMessageData( 6507943600L, "8D4BCDE99915851938048B4C6DA2" ),
            new RawMessageData( 6532434900L, "8D4D2228234994B7284820323B81" ),
            new RawMessageData( 6540584200L, "8D4D029F594B266AC774B7A0E396" ),
            new RawMessageData( 6540789400L, "8D4D029F9914E09BB828052F9BD6" ),
            new RawMessageData( 6565584200L, "8D4D022199108A1098600C87D596" ),
            new RawMessageData( 6569525800L, "8D4B1A00990CD695A8440AA591B2" ),
            new RawMessageData( 6586640700L, "8D4B1A1E5899B24BD999322D0F24" ),
            new RawMessageData( 6609334700L, "8D4CA2BF990C37B09018087AE4D9" ),
            new RawMessageData( 6638424800L, "8D4B17E19908EBA5E8A00CFC777A" ),
            new RawMessageData( 6649117100L, "8D4B17E55823E2AE455520B91485" ),
            new RawMessageData( 6653130500L, "8D3C648158AF92F907BCB973489D" ),
            new RawMessageData( 6658549900L, "8D4D029FEA1978667B5F042202CE" ),
            new RawMessageData( 6703087200L, "8D3C64819908E62EF8048B6231B9" ),
            new RawMessageData( 6709405000L, "8D4D0221EA0DC898015E182AAE02" ),
            new RawMessageData( 6711750100L, "8D44023758BF028353171EA5977F" ),
            new RawMessageData( 6733320500L, "8D4CA2BF606512FD3321B0C82882" ),
            new RawMessageData( 6734485300L, "8D440237EA485858013C08109653" ),
            new RawMessageData( 6739433700L, "8D4B29645949E68EAD8D3A89B626" ),
            new RawMessageData( 6739638900L, "8D4B29649910780C10480875242E" ),
            new RawMessageData( 6796662500L, "8D4B1A1E99091DA45080840405CC" ),
            new RawMessageData( 6857664800L, "8D39CEAA58AF8661E0F909DCD6FB" ),
            new RawMessageData( 6858084900L, "8D39CEAA99144E36000489FD23A1" ),
            new RawMessageData( 6881606700L, "8D49529958B32661D9561F63A10E" ),
            new RawMessageData( 6883614600L, "8D4B2964F8132006005AB8E3A7B3" ),
            new RawMessageData( 6938109300L, "8D3C6481EA42885C573C08357403" ),
            new RawMessageData( 6946551100L, "8D4D22289909451F18048C569633" ),
            new RawMessageData( 6965499200L, "8D4D022199108A10985C0C11139F" ),
            new RawMessageData( 7009169300L, "8D4D029F21286071DD3820D76F68" ),
            new RawMessageData( 7013032200L, "8D4D222860B9827B57488509E5CA" ),
            new RawMessageData( 7019252900L, "8D4B1A00583332BB6F63986D195F" ),
            new RawMessageData( 7019523400L, "8D4B1A00990CD695A8440AA591B2" ),
            new RawMessageData( 7059959800L, "8D4D029F594B22EEAB7DFDE94165" ),
            new RawMessageData( 7060165000L, "8D4D029F9914E09BB8240567C1D6" ),
            new RawMessageData( 7060665700L, "8D4D0221581302C9975F5E046266" ),
            new RawMessageData( 7157583600L, "8D39CEAA205161B7CF0DE02A2AAF" ),
            new RawMessageData( 7193198000L, "8D4B17E5234D74B6308820462627" ),
            new RawMessageData( 7201532100L, "8D4952999915779CF01C894EA576" ),
            new RawMessageData( 7203089700L, "8D4BCDE9234D84F51C88209BFD58" ),
            new RawMessageData( 7208021100L, "8D4B29645949E3134D972EB53A2A" ),
            new RawMessageData( 7208226400L, "8D4B29649910780C304808305807" ),
            new RawMessageData( 7228828000L, "8D4BCDE958B98682697297BCA460" ),
            new RawMessageData( 7253088900L, "8D3C64819908E62EF8048B6231B9" ),
            new RawMessageData( 7257254700L, "8D4B17E5990C8615A8440DAFCA10" ),
            new RawMessageData( 7306755600L, "8D4402372314A576CC8060E89CB9" ),
            new RawMessageData( 7363116900L, "8D3C6481F82300020049B832939F" ),
            new RawMessageData( 7380502000L, "8F01024C99256F1F58048C6C7104" ),
            new RawMessageData( 7382974700L, "8D4D02219910891098540D9B94C4" ),
            new RawMessageData( 7400051800L, "8D4D22289909451F18048C569633" ),
            new RawMessageData( 7403217000L, "8D39CEAA99144E36000489FD23A1" ),
            new RawMessageData( 7440752300L, "8D4D222860B985F95D408F53A49C" ),
            new RawMessageData( 7463177400L, "8D4D02215811F2C9A15F63B25366" ),
            new RawMessageData( 7465266700L, "8D4B17E5F8210002004BB8B1F1AC" ) );
    //<editor-fold desc="Message samples">
    private static final String SAMPLES_BASE64 = """
                                                 8AfzB/QH+gfqB/MH8wfyB+sH+Qf1B/QH+Qf6B+4H7gf2B/MH8wf9B+0H7wfxB/oH7Qf8B/QH/Af3B+0H
                                                 8Af2B/IH6gf1B/kH9QfvB/EH+wfyB/gH+Qf1B+gH8Qf2B+QH/wf5BwQI8gf3B+kH/QftB/QH+QfwB/EH
                                                 /gf5B/cH+AfuB/UH6QfyB/IH8wfvB+0H8Qf8B+wH8Qf6B/kH8Qf5B+0HAAj6BwAI9gf4B/QH6wf7B+wH
                                                 9AftB/QH8QfzB+4H/gfzB/AH9AfzB/sH+Qf7B/sH+Qf7B+wH/Af3B/IH8wfzB+kH8AfzB/YH6wf0B+0H
                                                 9AfuB/AH9wf6B/AH+wf6B/YH8wftBwEI9QfvB/kH8gf7B/IH8gf7B/QH8QfxB/EH+wfyB/cH8Af3B/YH
                                                 9wfzB/sH+Af0B+8H8wf6B+wH9AfxB/kH+AfxB/MH7QfzB/gH8gf+B/EH8Af7B/cH9Qf1B+kH9Qf1B+4H
                                                 8wfpB/sH7wf3B/EH/Qf+B/YH7gfnBwAI9Af0B/cH8Qf2B/EH8Af1B/IH8Qf0B/sH9wf2B/YH6wf2B+kH
                                                 /AfuB/YH9QfpB/AH9Af7B/8H+wf2B/YH7gf3B/AH6wfyB/IH+wcACPoH+Af2B/cH+Af6B+4H7gfvB/QH
                                                 6wf+B+wH9QfuB/QH/QfuB/QH7wf/B/gH/gfwB/MH9Af4B/UH9wf4B/UH+AfoB/sH8wfuB/IH9wfzB/QH
                                                 +Qf0B+sH9gcFCOAHAwgpCOwHuwfyBxUIAQjXB9gH/QcICPcH+Af7B/gH9AfwB+kH9wfvB74HBgglCOsH
                                                 vwf/BxMI+gfiB9sH9QfzBwII8wcBCPsHBAjuB/YH+gf+B/AH7wfuB/EH/gfwB/UH+wfqB/MH7wfyB/UH
                                                 7QfwB/YH8Af6B/oH9gfwB+sH+wf0B/IH9AfzB/EHCAjzB70HAAgdCPgHxwf8Bw8IDQjvB+4H7AfnB+sH
                                                 8gf1B/MH9AcFCAgI1gfWBxgIGQjbB9MHEggVCPcH7AfnB+AH9Af7B/YH+Af0B/cH8wf2B+kH6wfyBwEI
                                                 9Af0B/YH8wfxB/YH+wf3B/kH/Qf1B/cH8Qf+B+8H9gftB+sH9gf0B/IH+AfzB/gH9wfzB/cH7gfsB/cH
                                                 6wfwB/4H8wf0B/AH+QfyB+4H9Af1B/oH9QfuB+4HCwjbB8IHEggjCPgHzwcACBIIBgjpB+IH7AftB+0H
                                                 9Qf1B/oH9gfvB/kH9gf1B/QH7QftB/YH9wf8B/IH2gftBzAI+gfKB+AHGAgCCNoH2gf0B/oH+wcACP0H
                                                 /wf8B/IH6QfxBwAI8Ae+B/oHHwj8B8UH5QcVCAwI/gfoB/IH4gftB+8H5gf7B/MH7QcWCO4HwQf5By0I
                                                 8QfQB/QHLwgACMgH8gcUCAIIwQf2ByYIAgjKB9oH8Af2B/sH+Af2B/cH9Qf1B/YH/AfoB80H9AchCP0H
                                                 xwf6BxwICwjcB9wH8AfyB/8H+wf6B/MH9wf+B/sH9gf/B+sH/QcCCPcH7wfuB/AH9wf5B8wH3gcgCAcI
                                                 yAffBxsIBAjXB9kHHQgLCMQH2AccCAsI3AffB/0H/Af/B/YH/wfzB/UH9Af2B/MH9Qf1B/8H6wfsBwAI
                                                 9Qf1B/YH9QcBCOYHyAcGCCsI4QfBB/8HJQjgB70HAAgqCO0Hxgf9ByEI4wfJB+wHCggRCAQI/gfzB/AH
                                                 8gfxB+UH7wf1B/0H7wf9B/oH+QfzB+gH+Af3B/oHAQjfB9kHDwgVCNMH2AcICBYI/wfiB9sH7Qf2B/kH
                                                 7Af8B+4H+Af2B+0HDwgaCNcH1QcBCBsI5QfLB+gHAggACP8H+wfxB/wH7gf3B/kH3wfhBx4ICwjCB9YH
                                                 HAgICMgH4QcbCBMIxQfdByMICAjMB+YHIggGCO4H4wfxB+8H+Af0B/cH9gfuB/wH8gf9B/oH9AftB+MH
                                                 +AcACPkH7gftBwUICAjIB+QHIAgJCMUH1AcMCAUI9gfvB/EH6gfxB+kH+Af0B/gH9AflB+QHHwgVCMkH
                                                 4gcmCA0I0gfpBxwI/wfFB+QHHAgECMMH5wcOCBgI8AfsB+cH6gf0B+oH+wf4B/kH6wf/Bx8I3AfPBwgI
                                                 HwjlB+EHAQgICAQI+Qf3B+gH9AfrB/EH6QfvB/IH9gfsB/sH7Af6B/MH9gf8B/oH7QfsBx0I7AfBB/oH
                                                 HAjzB8sH8AcQCA4I7wfvB/IH9wfzB+cH5gftB/4H9AfMB/cHLQgBCM0H7QclCPYHxQf7ByYI7gfCB/0H
                                                 HgjzB8YH+AcNCAsI8Qf3B+oH6gfyB+8H+Af0B+gH4AcFCB4IyQfRBxUIBwjYB9sHCQgOCA4I/Qf/B/EH
                                                 7wfyB/kH9QfzB98HFAj8B7oH1gcVCA8I0gfeBwEICQj+B/4H9wf0B/AH8gfzB/QH8QfxB/AH7QftB/sH
                                                 /Af4B/sH8wf7B9sH6wcwCAMIvAfcBywI+gfSB94H+AcBCPUHAQj9B/YH8Qf2B+gH6wcHCPoHvgfnBxUI
                                                 AgjKB+wHJgj4B8YH8wcjCPwHzQf9BxsI9wfTB+kH9Qf+B/YH+QfyB/UHAQj9B+4H9gfkB8IHEAgiCNYH
                                                 1gcLCBoI8gfSB9sH9gf6B/sH/wftB/kH+QcDCPQH9wfuB/8H7Qf2B/AH8Af7B+8H+Qf+B8cH2QcwCAUI
                                                 ygfkByEI9gfEB98HKggACMYH7gcmCPIH1AfnBwsIDwj1B/oH+Qf5B/EH9wfqB+0H+gfxBwAI8wf2B+4H
                                                 +Qf3B/cH9AftBwoI3QfNBxYIIQjeB80HDQgaCPYH6AfnB+wH8gfwB+4H/wf3B/sH+QfeBxQIHgjYB9MH
                                                 DggcCNgH0gfzBwQIAAj8B/gH7QftB+0H8QfpB/QH3AcICB4I1AfNBxAIFQjdB9wH9Af/BwEIBAj3B/EH
                                                 8wfzB+kH8gftB9QHFwgYCNAHzQcOCA4I0AfaBx4IEAjOB9MHIggRCMwH4QcXCAoI5wfrB+4H+QfrB/MH
                                                 +wfmB/YH+AcECPoH/QcACPEH/QfzB/IH6QfxB+MHBwgGCL8H4gcjCAQI0gflByMIAQi/B+4HGAj6B8sH
                                                 8gcWCPgH0gfgB/MHBggOCPYH/QcBCPUH9Qf+B+wH6AccCPYHwAfqBxEI7AfSB+IHBggNCO4H+Af6B+0H
                                                 8QfxB+YH8AfrB/YH/wf1B/UH/Qf6B/cH9wf0B+8H7QcdCA0ItgfjByEI/wfOB+MHAggFCPMH/gfsB/EH
                                                 /QfzB+oH9gf2B+8HvAf0ByEI6gfLB/EHHwj9B+MH4Af5B/QHAQgACAII9wf2B/MHBAjgB9UHGwgVCNkH
                                                 1gcXCBEIvwfYBxwIFAjMB9QHIAgMCMMH1AcNCBcI+wfvB+4H/AflB+0H/QfvB/MH7wf1B/kH+Qf/B/kH
                                                 /QfyB/0H+wfyB/gHFggCCLkH5QcfCOgHuQfrBw8I7QfGB/sHGwjtB8AH/QceCPcH3gfhB/gH9wf4B/kH
                                                 /QcACPYHAgj7BwEI9Qf5B/cH5Qf0B/YH8Qf3B+sH5wcYCAAIuAfdBxoIAwjHB+gH+gcJCPgH+Af0B/QH
                                                 +gf8B+8H+gf8B/8HywfnBy8I6wfJB/sHGwgECOAH1QfjB/cHAQjoB/QHAwj5B/YHCAjrB8kHDAg1CPEH
                                                 0QcECB4I0gfLBwoIGQjhB8YHEAgWCNQHwwcBCA4IAwj3B+kH6AfxB+0H9wf0B/4H9Af0BxQIEQjOB9EH
                                                 HwgaCNoH2Af0Bw0I9AcDCPgH9AcBCPIH7Qf2B+sH9wceCNgHuQcACBsI4wfCB/0HCQgACO8H9gf8B/sH
                                                 6gfyB/IH8QfoB/sH+gf1B+4H+Qf3B/0H7wfzB/EH8gcfCAMIugfoByAI/QfJB94HBAgACAII8gf+B/IH
                                                 +AfxB/AH6gfxBwQI1wfOBxUIHwjPB9AHFggQCPQH4AfoB+oHAQj8B/IH9Af8B/QHBQgQCOUH2gceCBMI
                                                 0gfVBw8IDgjqB9sH6AfsB+8H+wf2B+sH8gf2B+8HCgjTB9QHGAgYCNUH2QckCA4IyAfYBygIEQjQB94H
                                                 KAj6B80H4Af4BxII9Qf8B/YH8wfxB/UH9wf0B/cH2wfjByMIBQjAB+UHJwgBCNkH3wf1B/YHBwgDCO8H
                                                 9gfxB/sH8gf6B/IH8gfoB/QH+QftB/cH8Af1BwAI8wfLB/0HLAj0B8QHBQgcCPcH2gflB+8HBAj0B/oH
                                                 +gf4B/8H+AfrB/MHGQjhB8UHBggfCNsH0Af7Bw0I/gf6B+8H9Qf5B+wH8QfsB/IH6wcICAQIygfSBxoI
                                                 /wfYB+cHGQgLCP8H5QfvB+4H9QftB/AH7QfxB+oHAggMCMoH2AcXCAoI0gfcBxIICggCCO0H8wfuB+4H
                                                 7gfxB/cH8QfpB/wHGAjSB8MHGggjCN4H1QcaCCUI0QfQBx0IFAjSB9MHEQgMCN8H3QfrBwEI8QfxB/kH
                                                 7QfyB/YH+gfqB/cH9wfqB/cH8QfxB/oH9wfxB/QHBgjZB88HHggNCNIHzQcXCAsI2AfeByEICgjNB+AH
                                                 GwgRCMwH7Qf3BwIIBQj4B/cH9gf0B/oH4gfqB+QHCwjoB7wH9AcfCPQHwgfxBxYICAjpB+kH8AfvB/QH
                                                 9QfxB/UH+wf9B/4H8gf5B/8HAgj8B/IH9gftB+gHAQj0B8MH4gcmCP8HugfuBwsIDAjuB/gH5wfyB/MH
                                                 9AfwB/MH8gfzB9wH+gcmCOsH0QcGCBsI5QfdB/IHBAj8B/kH/wf2B/kH6wf1B/MH6wfdBw8IEgjPB8gH
                                                 EggGCNYH2Af0Bw0IAAgCCPkHAAjzB+EH8gf0B/YH2gcACCMIyQfLBwUIEQjQB9cHIQgUCM4H1AcnCAsI
                                                 0gfaBx0ICAjyB+oH5gfvB/gH9gf0B/YH+Qf1B+sH9AfrB/cH7Qf7B/kH+Af1B+0H6QcDCAcIvwfbByMI
                                                 DgjOB+AHCwgVCAUI5gfpB+sH+wfrB/kH7AfsB/QH5gfhByMIAgjMB+UHIgj9B8gH+QchCAAIyQf6Bx4I
                                                 9QfNB/QHEAgDCOEH7AftB+0H7wfnB/IH8Af9B/MH+gf5B/cH8wfvB/QH+gf4B/8H7gcFCAgIvwfdBygI
                                                 CQjOB90HIQgLCMkH5gcVCAYIygflBxAIAQjVB+IH9QcFCP8H+Qf4B/wHBgjmB/cH8Qf4B+8H9AfyB/QH
                                                 7wfrB/oH9wfwBwoIBgi8B+oHHAgVCMUH4gcVCBAI9AfqB+0H8QfzB/IH8gf5B/sH7gfhB+4HLQj7B8UH
                                                 /QceCOkH3gfbB/YHBQj2B/0H9wcDCAAI8wfzB+sH4AcICBYI0ge/BxgIHgjKB88HEggYCNYHzAceCBcI
                                                 2gfSBw8IBQj2B98H8QflB/gH7gfyB/0HAAjqB+0HAwgXCM0HzgccCA8I1AfSBw8IAgj7B/EH7AfsB/EH
                                                 8AfuB/EH8gfwB/IH8gf5B/wH8wfvBwMIAwj2B+8H+QcXCNgHygcZCBMI1gfZBwEIEAjxB/EH9AfqB/YH
                                                 4wfuB/UH+gfxB9IH8wcgCPwHuwf2Bx4I8AfFB/gHKwj0B8YH9QcjCO4Hywf9BxgI/wfoB+4H6wfwB/AH
                                                 9gf5B/EH6AfyB/kH/AfyB/YH6wf3B/AH+gf7B+wHCAgdCM0H2QcYCBEIzQfiByEIEgjJB+MHFQgKCMgH
                                                 2QcpCAkI2gfcB+8H+gf+B/EH9Qf9B/YH9gf3B+0HEAgCCM8H3gcYCBgIyAfjBwUIEQj8B/UH7QfvB/EH
                                                 9AfpB+wH7Af8B/UH9gf5B/kH8gftB/UH/Af1B/QH+wcXCOsHuAf8Bx8I+AfFB/gHEgjtB8kH/QcmCOcH
                                                 zAcGCCAI9QfaB90H9Af3B/IH9QcACPgH+wf4B/gH8QceCAEIxAfkBx4IAAjHB+YH/gcICPUH+wfyB/cH
                                                 4wfnBwMI+gfwB/kH7gf/B/cH/Af9B+oH/wfwB+wH5gcYCAQIygfjBxwIAwjOB+AH+wcOCAMI9gfyB/kH
                                                 5wfuB+kH7gcFCPcHzgf3BysI8gfIB/sHKAjnB9AH9wchCOQHxQf6ByII9AfIB/UHCggMCPcH9Qf5B/cH
                                                 /AfrB+4H9gfzB9IH/wclCNQHuwf6BxcI7QfVB+oHAwj6BwYIBAj4B/sH/wf9B/AH5wfmByQI9AerB+0H
                                                 IwjmB8gH9Qf/BwgIAgj2B/MH7wfsB+wH8Af3B/cH6QccCAMItwfrBxsIBgjHB+QH9gcLCAAI/AfxBwEI
                                                 /gfwB+4H7gf0B/QH8wf0B/AH9Af1B/IH/Qf6B/YH2wfuBy0I9wfAB/IHKAjqB8MH+wcfCO0HyQcECCII
                                                 6AfIBwAIGAgQCPMH1wfyB+wH7gfvB/MH9wf5B/IH8wf5B/gH9Qf2B/EH9AcICPcH5wfdB+0HNwjgB8cH
                                                 +AceCOcH2wfrB/sH/wfyB/oH/Af5B/QH8AfsB/QHAwjpB8kHBQgYCOQHyAcQCCMI4wfEBxYIHAjjB8oH
                                                 DwgeCN8H2gfsB/gH/wcDCPIH9gf1B/QH8wf2B/YH2wfmBzII+gfCB+sHKQj9B9cH1AfsB/0H9AcECPsH
                                                 AQjyB/gH8wfxB/sH7AfvB+0H/gfsB/IH7gfrB/cH5gfXBywICgjXB9cHGQgaCMMH5wcTCA4I0gfdBxMI
                                                 Egi9B+IHAAgECPwH8wf6B/wH9AfjB+8H5wfuB/YH7gfjBxIIFgjaB9MHGQgbCOAH1QfjB/YH9wfzB/sH
                                                 9Qf5BwEI7wfyB+wH9AfxB/wH8QfqB+8H8wfwBwII4wfPBxsIFgjZB8sHFQgRCNAH1AcVCA8I2QfVBxEI
                                                 FAjQB9cHBwgYCAII+wfvB+wH+gf0B/EH9gf1B/0H5Qf4B+oH6wfwB+oH/wfzB/kH+AfjB+QHJggOCNgH
                                                 1AcXCBEI3gfiB+8H/wcACPsH9gf7B/EH9Af3B/MHAggICLgH8gchCOoHxgf6BxwI8AfDB/sHHQjsB8wH
                                                 9gciCPYH2gfpB/gH/wf7B/wH9wcBCP8H7Af1B+8H9QfuB/8H8gfxB/YH7wf4B+UH8gfdB/QHHAjyB8MH
                                                 9gcfCO4HxwfzByMI/AfKB/kHLAj5B7sH9QcXCAcI9QfsB+sH6wftB+kH5gf3B+UHBgjbB9UHHAgcCNIH
                                                 zgcPCBsI5wfaB+oH8gf4B/kHAAj6B/YH+QfxB+sHAQjtBwQI4wfxB/IH6gf4B/cHAQjlB94HKggHCLkH
                                                 5AcQCBMI2QfcB/wH8wcMCP8H+gcFCPwH8wf0B/UH7wfvB+sH9AfoB/MH9QfzB/MH5wf4B/EH8Af/B/4H
                                                 6wf7B/AHAAj6B/4H8wfyB/AH+AfsB+0H9QfvB/cH+QfsB/cH9wfxB/cHAwjxB/0H9wf1B/MH/wfjB/sH
                                                 8Af2B/EH9wfwB/AH8QfnB/QH8Qf4B/EH+QftB/MH6gf1B/gH8gcCCPIHAAj4B/kH7Qf2B/AH9wf5B+kH
                                                 9wf1B/UH/Af3B+4H6gf1B/EH9wfyB/4H6Qf2B/AH9AfxB/EHAgj9B/QH8QcECPIH+wf0B/UH8wfzB/MH
                                                 +Af3B/4H9AfyB/sH8AfpB/UH+AfxB/gH/wf5B/QH7wf2B/UH7wfwB/gH+gfyB/EH7QflB/sH+AftB/cH
                                                 9gf3B/IH8wftBwAI8wf6B/AH8gf3B+oH8Af3B/gH8wf6B+QH9QftB/gH+wfvB/AH+AfwB/QH7wfsB/kH
                                                 +gfzB+4H/gfwB/wH9wfwB/oH9gf1B/0H9Qf8B/MH/Af5B/UH5QfzB/QH8QfwB/UH9QfzB+gH7gf0B/UH
                                                 9Qf6B/cH/gfzB/sH9wf6B/AH7Af2B/YH+wfvB/AH8Qf0B/cH/wfxB+kH8gftB/IH9gf6B/QH8wfuB/YH
                                                 8wf2B/cH8Qf0B+4H8wf8B+8H+Af1B/gH9Qf5B/IH+Qf2B/sH8Qf3B/kH+Af1B+0H6wfyB/AH9wf3BwQI
                                                 9wfwB/gH7wf2B/kH7QfzB+wH9QfuB+4H+AfyB/gH/gf2B+oH8gf3B/YH+QfyB/EH8gf6B/EH8wfyB/sH
                                                 8Qf5B+wH+gfzB/8H8Qf0B/oH6gf4B/kH9QfvB/YH9Af8B+oH8Qf2B/QH+wf2B/UH8gf2B+4H9wf8B/EH
                                                 """;


    @BeforeAll
    static void preventOutput() {
        if ( System.getProperty( "ch.epfl.cs108.quiet" ) != null ) {
            System.setOut( new PrintStream( OutputStream.nullOutputStream() ) );
            System.setErr( new PrintStream( OutputStream.nullOutputStream() ) );
        }
    }

    //    public static void main(String[] args) throws IOException {
    //        testAircraftIdentificationMessageOf();
    //        testAirbornePositionMessageOf();
    //    }


    public static void testAircraftIdentificationMessageOf() throws IOException {
        String f = "C:\\Users\\Eva Mangano\\OneDrive\\Documents\\EPFL\\4 - BA2\\PROJET\\Javions\\resources\\samples_20230304_1442.bin";
        try ( InputStream s = new FileInputStream( f ) ) {
            AdsbDemodulator d = new AdsbDemodulator( s );
            RawMessage m;

            int index = 0;
            while ( ( m = d.nextMessage() ) != null ) {
                if ( isIdentificationMessage( m ) ) {
                    AircraftIdentificationMessage message = AircraftIdentificationMessage.of( m );
                    index++;
                }
            }
        }
    }


    public static void testAirbornePositionMessageOf() throws IOException {
        String f = "C:\\Users\\Eva Mangano\\OneDrive\\Documents\\EPFL\\4 - BA2\\PROJET\\Javions\\resources\\samples_20230304_1442.bin";
        try ( InputStream s = new FileInputStream( f ) ) {
            AdsbDemodulator d = new AdsbDemodulator( s );
            RawMessage m;

            int index = 0;
            while ( ( m = d.nextMessage() ) != null ) {
                if ( isPositionMessage( m ) ) {
                    AirbornePositionMessage message = AirbornePositionMessage.of( m );
                    index++;
                }
            }
        }
    }


    private static boolean isIdentificationMessage(RawMessage rawMessage) {
        int typeCode = rawMessage.typeCode();
        return typeCode == 1 || typeCode == 2 || typeCode == 3 || typeCode == 4;
    }


    private static boolean isPositionMessage(RawMessage rawMessage) {
        int typeCode = rawMessage.typeCode();
        return ( 9 <= typeCode && typeCode <= 18 ) || ( 20 <= typeCode && typeCode <= 22 );
    }


    @Test
    void airbornePositionMessageConstructorThrowsWhenTimeStampIsInvalid() {
        assertThrows( IllegalArgumentException.class, () -> {
            new AirbornePositionMessage( -1, new IcaoAddress( "ABCDEF" ), 1000, 0, 0, 0 );
        } );
        assertDoesNotThrow( () -> {
            new AirbornePositionMessage( 0, new IcaoAddress( "ABCDEF" ), 1000, 0, 0, 0 );
        } );
    }


    @Test
    void airbornePositionMessageConstructorThrowsWhenIcaoAddressIsNull() {
        assertThrows( NullPointerException.class, () -> {
            new AirbornePositionMessage( 100, null, 1000, 0, 0, 0 );
        } );
    }


    @Test
    void airbornePositionMessageConstructorThrowsWhenParityIsInvalid() {
        var icaoAddress = new IcaoAddress( "ABCDEF" );
        for ( int i = -100 ; i <= 100 ; i += 1 ) {
            if ( i == 0 || i == 1 ) {
                continue;
            }
            var invalidParity = i;
            assertThrows( IllegalArgumentException.class, () -> {
                new AirbornePositionMessage( 100, icaoAddress, 100, invalidParity, 0.5, 0.5 );
            } );
        }
    }


    @Test
    void airbornePositionMessageConstructorThrowsWhenXYAreInvalid() {
        var icaoAddress = new IcaoAddress( "ABCDEF" );
        for ( var invalidXY = 1d ; invalidXY < 5d ; invalidXY += 0.1 ) {
            var xy = invalidXY;
            assertThrows( IllegalArgumentException.class, () -> {
                new AirbornePositionMessage( 100, icaoAddress, 100, 0, xy, 0.5 );
            } );
            assertThrows( IllegalArgumentException.class, () -> {
                new AirbornePositionMessage( 100, icaoAddress, 100, 0, -xy, 0.5 );
            } );
            assertThrows( IllegalArgumentException.class, () -> {
                new AirbornePositionMessage( 100, icaoAddress, 100, 0, 0.5, xy );
            } );
            assertThrows( IllegalArgumentException.class, () -> {
                new AirbornePositionMessage( 100, icaoAddress, 100, 0, 0.5, -xy );
            } );
        }
    }


    @Test
    void airbornePositionMessageOfCorrectlyDecodesAltitudeWhenQIs0() {
        record MessageAndAltitude(String message, double altitude) {

        }
        var testValues = List.of( new MessageAndAltitude( "8D4B1BB5598486491F4BDBF44FC6", 1584.96 ),
                                  new MessageAndAltitude( "8D4B1BB5592C22D2A155F49835EF", 1798.32 ),
                                  new MessageAndAltitude( "8D4B1BB5592422D2BB55FD991FA4", 1828.80 ),
                                  new MessageAndAltitude( "8D4B1BB559A4264FDB4DDDC058EA", 1859.28 ),
                                  new MessageAndAltitude( "8D4B1BB5598426509F4E1F032D5D", 1889.76 ),
                                  new MessageAndAltitude( "8D4B1BB5598406514D4E5FEC1AC3", 1920.24 ),
                                  new MessageAndAltitude( "8D4B1BB559A40653594F35F9A08F", 1950.72 ),
                                  new MessageAndAltitude( "8D4B1BB55924065661506DA3728A", 1981.20 ),
                                  new MessageAndAltitude( "8D4B1BB5592C02EE175E9AF78185", 2011.68 ),
                                  new MessageAndAltitude( "8D4B1BB5590C02FAED63A05783E1", 2042.16 ),
                                  new MessageAndAltitude( "8DADA2FD593682D9D99C2643E7DA", 2743.20 ) );

        for ( MessageAndAltitude testValue : testValues ) {
            var message = RawMessage.of( 100, HexFormat.of()
                                                       .parseHex( testValue.message ) );
            assertNotNull( message );
            var airbornePositionMessage = AirbornePositionMessage.of( message );
            assertNotNull( airbornePositionMessage );
            assertEquals( testValue.altitude, airbornePositionMessage.altitude(), 0.005 );
        }
    }


    @Test
    void airbornePositionMessageOfCorrectlyDecodesAltitudeWhenQIs1() {
        record MessageAndAltitude(String message, double altitude) {

        }
        var testValues = List.of( new MessageAndAltitude( "8D406666580D1652395CBE0A4D3E", 434.34 ),
                                  new MessageAndAltitude( "8D4B1BDD5911A68127785A8F1273", 746.76 ),
                                  new MessageAndAltitude( "8D344645584592A80D5BC637ED82", 3909.06 ),
                                  new MessageAndAltitude( "8F405B66585915E28714229EFD13", 5067.30 ),
                                  new MessageAndAltitude( "8D4B1A23586B8307F5B26CB39D00", 6217.92 ),
                                  new MessageAndAltitude( "8D4402F2587563156B9880D4D855", 6812.28 ),
                                  new MessageAndAltitude( "8D4402F25887D6AFD7A1A3769B45", 7962.90 ),
                                  new MessageAndAltitude( "8D347307589B66396B69C91DD7B1", 9128.76 ),
                                  new MessageAndAltitude( "8D4CA24558ADE68009DEF6E531E5", 10287.00 ),
                                  new MessageAndAltitude( "8D49328A59CB16A537939E3B583D", 12016.74 ),
                                  new MessageAndAltitude( "8D3B754358D311E57545B2100575", 12504.42 ) );
        for ( var testValue : testValues ) {
            var message = RawMessage.of( 100, HexFormat.of()
                                                       .parseHex( testValue.message ) );
            assertNotNull( message );
            var airbornePositionMessage = AirbornePositionMessage.of( message );
            assertNotNull( airbornePositionMessage );
            assertEquals( testValue.altitude, airbornePositionMessage.altitude(), 0.005 );
        }
    }


    // Code to generate the invalid messages used by the test below.
    List<String> airbornePositionMessagesWithInvalidAltitude() {
        var crcComputer = new Crc24( Crc24.GENERATOR );
        var messages = new ArrayList<String>();

        var byte0 = "8D";
        var icaoAddress = "406666";
        var payload = 0x580D1652395CBEL;
        var altMask = ( ( 1L << 12 ) - 1 ) << 36;
        var invalidAlts = new long[]{0b000000000000, 0b101010000000, 0b100010000000};
        for ( var alt : invalidAlts ) {
            var corruptedPayload = payload & ~altMask | ( alt & 0xFFF ) << 36;
            var messageWithoutCRC = byte0 + icaoAddress + "%014X".formatted( corruptedPayload );
            var messageBytes = HexFormat.of()
                                        .parseHex( messageWithoutCRC );
            var crc = crcComputer.crc( messageBytes );
            var message = messageWithoutCRC + "%06X".formatted( crc );
            messages.add( message );
        }
        return messages;
    }


    @Test
    void airbornePositionMessageOfReturnsNullWhenAltitudeIsInvalid() {
        var messages = List.of( "8D40666658000652395CBEB25722", "8D40666658A80652395CBED10630",
                                "8D40666658880652395CBE7570E9" );
        for ( var testValue : messages ) {
            var message = RawMessage.of( 100, HexFormat.of()
                                                       .parseHex( testValue ) );
            assertNotNull( message );
            var airbornePositionMessage = AirbornePositionMessage.of( message );
            assertNull( airbornePositionMessage );
        }
    }


    private record RawMessageData(long timeStampNs, String bytes) {

    }
}

/*
"C:\Users\Eva Mangano\.jdks\temurin-17.0.6\bin\java.exe" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.3\lib\idea_rt.jar=59551:C:\Program Files\JetBrains\IntelliJ IDEA 2021.3\bin" -Dfile.encoding=UTF-8 -classpath "C:\Users\Eva Mangano\OneDrive\Documents\EPFL\4 - BA2\PROJET\out\test\Javions;C:\Users\Eva Mangano\OneDrive\Documents\EPFL\4 - BA2\PROJET\out\production\Javions;C:\Users\Eva Mangano\.m2\repository\org\junit\jupiter\junit-jupiter\5.9.2\junit-jupiter-5.9.2.jar;C:\Users\Eva Mangano\.m2\repository\org\junit\jupiter\junit-jupiter-api\5.9.2\junit-jupiter-api-5.9.2.jar;C:\Users\Eva Mangano\.m2\repository\org\opentest4j\opentest4j\1.2.0\opentest4j-1.2.0.jar;C:\Users\Eva Mangano\.m2\repository\org\junit\platform\junit-platform-commons\1.9.2\junit-platform-commons-1.9.2.jar;C:\Users\Eva Mangano\.m2\repository\org\apiguardian\apiguardian-api\1.1.2\apiguardian-api-1.1.2.jar;C:\Users\Eva Mangano\.m2\repository\org\junit\jupiter\junit-jupiter-params\5.9.2\junit-jupiter-params-5.9.2.jar;C:\Users\Eva Mangano\.m2\repository\org\junit\jupiter\junit-jupiter-engine\5.9.2\junit-jupiter-engine-5.9.2.jar;C:\Users\Eva Mangano\.m2\repository\org\junit\platform\junit-platform-engine\1.9.2\junit-platform-engine-1.9.2.jar" ch.epfl.javions.adsb.AirbornePositionMessageTest
AircraftIdentificationMessage[timeStampNs=1499146900, icaoAddress=IcaoAddress[string=4D2228], category=163, callSign=CallSign[string=RYR7JD]]
AircraftIdentificationMessage[timeStampNs=2240535600, icaoAddress=IcaoAddress[string=01024C], category=163, callSign=CallSign[string=MSC3361]]
AircraftIdentificationMessage[timeStampNs=2698727800, icaoAddress=IcaoAddress[string=495299], category=163, callSign=CallSign[string=TAP931]]
AircraftIdentificationMessage[timeStampNs=3215880100, icaoAddress=IcaoAddress[string=A4F239], category=165, callSign=CallSign[string=DAL153]]
AircraftIdentificationMessage[timeStampNs=4103219900, icaoAddress=IcaoAddress[string=4B2964], category=161, callSign=CallSign[string=HBPRO]]
AircraftIdentificationMessage[timeStampNs=4619251500, icaoAddress=IcaoAddress[string=4B1A00], category=162, callSign=CallSign[string=SAZ54]]
AircraftIdentificationMessage[timeStampNs=4750361000, icaoAddress=IcaoAddress[string=4D0221], category=162, callSign=CallSign[string=SVW29VM]]
AircraftIdentificationMessage[timeStampNs=5240059000, icaoAddress=IcaoAddress[string=4CA2BF], category=163, callSign=CallSign[string=RYR5907]]
AircraftIdentificationMessage[timeStampNs=6532434900, icaoAddress=IcaoAddress[string=4D2228], category=163, callSign=CallSign[string=RYR7JD]]
AircraftIdentificationMessage[timeStampNs=7009169300, icaoAddress=IcaoAddress[string=4D029F], category=161, callSign=CallSign[string=JFA17S]]
AircraftIdentificationMessage[timeStampNs=7157583600, icaoAddress=IcaoAddress[string=39CEAA], category=160, callSign=CallSign[string=TVF7307]]
AircraftIdentificationMessage[timeStampNs=7193198000, icaoAddress=IcaoAddress[string=4B17E5], category=163, callSign=CallSign[string=SWR6LH]]
AircraftIdentificationMessage[timeStampNs=7203089700, icaoAddress=IcaoAddress[string=4BCDE9], category=163, callSign=CallSign[string=SXS5GH]]
AircraftIdentificationMessage[timeStampNs=7306755600, icaoAddress=IcaoAddress[string=440237], category=163, callSign=CallSign[string=EJU63HA]]
AirbornePositionMessage[timeStampNs=75898000, icaoAddress=IcaoAddress[string=495299], altitude=10546.08, parity=0, x=0.6867904663085938, y=0.7254638671875]
AirbornePositionMessage[timeStampNs=116538700, icaoAddress=IcaoAddress[string=4241A9], altitude=1303.02, parity=0, x=0.702667236328125, y=0.7131423950195312]
AirbornePositionMessage[timeStampNs=138560100, icaoAddress=IcaoAddress[string=4D2228], altitude=10972.800000000001, parity=1, x=0.6243515014648438, y=0.4921417236328125]
AirbornePositionMessage[timeStampNs=208135700, icaoAddress=IcaoAddress[string=4D029F], altitude=4244.34, parity=0, x=0.747222900390625, y=0.7342300415039062]
AirbornePositionMessage[timeStampNs=233069800, icaoAddress=IcaoAddress[string=3C6481], altitude=10370.82, parity=0, x=0.8674850463867188, y=0.7413406372070312]
AirbornePositionMessage[timeStampNs=349256100, icaoAddress=IcaoAddress[string=4B1A00], altitude=2796.54, parity=1, x=0.6786422729492188, y=0.5558624267578125]
AirbornePositionMessage[timeStampNs=587873700, icaoAddress=IcaoAddress[string=4D2228], altitude=10972.800000000001, parity=0, x=0.64007568359375, y=0.6192169189453125]
AirbornePositionMessage[timeStampNs=600759300, icaoAddress=IcaoAddress[string=39D300], altitude=11582.400000000001, parity=0, x=0.624267578125, y=0.6164398193359375]
AirbornePositionMessage[timeStampNs=612521000, icaoAddress=IcaoAddress[string=4BCDE9], altitude=10972.800000000001, parity=1, x=0.7257156372070312, y=0.6263427734375]
AirbornePositionMessage[timeStampNs=633078600, icaoAddress=IcaoAddress[string=3C6481], altitude=10370.82, parity=0, x=0.8675537109375, y=0.741455078125]
AirbornePositionMessage[timeStampNs=645590600, icaoAddress=IcaoAddress[string=4D029F], altitude=4244.34, parity=0, x=0.7471542358398438, y=0.734161376953125]
AirbornePositionMessage[timeStampNs=715212600, icaoAddress=IcaoAddress[string=4D0221], altitude=838.2, parity=0, x=0.6855926513671875, y=0.696197509765625]
AirbornePositionMessage[timeStampNs=797810600, icaoAddress=IcaoAddress[string=4CA2BF], altitude=5791.200000000001, parity=0, x=0.5660781860351562, y=0.7490234375]
AirbornePositionMessage[timeStampNs=812236600, icaoAddress=IcaoAddress[string=4951CE], altitude=6705.6, parity=0, x=0.8513717651367188, y=0.7908096313476562]
AirbornePositionMessage[timeStampNs=857674200, icaoAddress=IcaoAddress[string=39CEAA], altitude=10363.2, parity=1, x=0.48673248291015625, y=0.5936203002929688]
AirbornePositionMessage[timeStampNs=1008567300, icaoAddress=IcaoAddress[string=495299], altitude=10546.08, parity=0, x=0.6865463256835938, y=0.725311279296875]
AirbornePositionMessage[timeStampNs=1032331100, icaoAddress=IcaoAddress[string=4B17E5], altitude=1905.0, parity=0, x=0.6668930053710938, y=0.66949462890625]
AirbornePositionMessage[timeStampNs=1127252300, icaoAddress=IcaoAddress[string=4D2228], altitude=10972.800000000001, parity=1, x=0.6245880126953125, y=0.49233245849609375]
AirbornePositionMessage[timeStampNs=1138542200, icaoAddress=IcaoAddress[string=4241A9], altitude=1310.64, parity=1, x=0.68560791015625, y=0.5846710205078125]
AirbornePositionMessage[timeStampNs=1184750000, icaoAddress=IcaoAddress[string=4D029F], altitude=4236.72, parity=1, x=0.728851318359375, y=0.6051712036132812]
AirbornePositionMessage[timeStampNs=1214082000, icaoAddress=IcaoAddress[string=394C13], altitude=10668.0, parity=1, x=0.44959259033203125, y=0.47266387939453125]
AirbornePositionMessage[timeStampNs=1223069500, icaoAddress=IcaoAddress[string=3C6481], altitude=10370.82, parity=0, x=0.8676605224609375, y=0.7416305541992188]
AirbornePositionMessage[timeStampNs=1244978500, icaoAddress=IcaoAddress[string=4D0221], altitude=822.96, parity=1, x=0.6689300537109375, y=0.5679931640625]
AirbornePositionMessage[timeStampNs=1399281900, icaoAddress=IcaoAddress[string=4B1A00], altitude=2788.92, parity=0, x=0.6954269409179688, y=0.6837844848632812]
AirbornePositionMessage[timeStampNs=1437501700, icaoAddress=IcaoAddress[string=39CEAA], altitude=10363.2, parity=1, x=0.48670196533203125, y=0.5938186645507812]
AirbornePositionMessage[timeStampNs=1515918300, icaoAddress=IcaoAddress[string=39D300], altitude=11582.400000000001, parity=1, x=0.6088943481445312, y=0.48973846435546875]
AirbornePositionMessage[timeStampNs=1580887900, icaoAddress=IcaoAddress[string=4D2228], altitude=10972.800000000001, parity=0, x=0.6403121948242188, y=0.6194076538085938]
AirbornePositionMessage[timeStampNs=1581106900, icaoAddress=IcaoAddress[string=4BCDE9], altitude=10972.800000000001, parity=0, x=0.7435684204101562, y=0.7557525634765625]
AirbornePositionMessage[timeStampNs=1645019700, icaoAddress=IcaoAddress[string=4D029F], altitude=4236.72, parity=0, x=0.7469863891601562, y=0.7339935302734375]
AirbornePositionMessage[timeStampNs=1656866000, icaoAddress=IcaoAddress[string=440237], altitude=11277.6, parity=1, x=0.533203125, y=0.5020675659179688]
AirbornePositionMessage[timeStampNs=1688150900, icaoAddress=IcaoAddress[string=3C6481], altitude=10370.82, parity=0, x=0.8677291870117188, y=0.7417449951171875]
AirbornePositionMessage[timeStampNs=1714855000, icaoAddress=IcaoAddress[string=4CA2BF], altitude=5791.200000000001, parity=0, x=0.5660247802734375, y=0.748748779296875]
AirbornePositionMessage[timeStampNs=1762693600, icaoAddress=IcaoAddress[string=4951CE], altitude=6705.6, parity=1, x=0.8307952880859375, y=0.6611328125]
AirbornePositionMessage[timeStampNs=1835102700, icaoAddress=IcaoAddress[string=4D0221], altitude=822.96, parity=0, x=0.6857147216796875, y=0.6963272094726562]
AirbornePositionMessage[timeStampNs=1896411000, icaoAddress=IcaoAddress[string=4B17E5], altitude=1897.38, parity=0, x=0.6667861938476562, y=0.6695785522460938]
AirbornePositionMessage[timeStampNs=1955871700, icaoAddress=IcaoAddress[string=A4F239], altitude=10363.2, parity=1, x=0.5801239013671875, y=0.350616455078125]
AirbornePositionMessage[timeStampNs=2011173700, icaoAddress=IcaoAddress[string=495299], altitude=10546.08, parity=1, x=0.669525146484375, y=0.5963821411132812]
AirbornePositionMessage[timeStampNs=2075921200, icaoAddress=IcaoAddress[string=4D029F], altitude=4236.72, parity=0, x=0.746917724609375, y=0.7339248657226562]
AirbornePositionMessage[timeStampNs=2076864400, icaoAddress=IcaoAddress[string=4241A9], altitude=1325.88, parity=0, x=0.70281982421875, y=0.7133026123046875]
AirbornePositionMessage[timeStampNs=2118341200, icaoAddress=IcaoAddress[string=4D2228], altitude=10972.800000000001, parity=1, x=0.6248245239257812, y=0.49251556396484375]
AirbornePositionMessage[timeStampNs=2186271000, icaoAddress=IcaoAddress[string=4B2964], altitude=4160.52, parity=1, x=0.7754287719726562, y=0.6389999389648438]
AirbornePositionMessage[timeStampNs=2283115100, icaoAddress=IcaoAddress[string=3C6481], altitude=10370.82, parity=0, x=0.8678359985351562, y=0.7419204711914062]
AirbornePositionMessage[timeStampNs=2289331500, icaoAddress=IcaoAddress[string=4B1A00], altitude=2788.92, parity=0, x=0.6952972412109375, y=0.6836700439453125]
AirbornePositionMessage[timeStampNs=2345132300, icaoAddress=IcaoAddress[string=4D0221], altitude=822.96, parity=0, x=0.685760498046875, y=0.6963653564453125]
AirbornePositionMessage[timeStampNs=2367505900, icaoAddress=IcaoAddress[string=39CEAA], altitude=10363.2, parity=0, x=0.49881744384765625, y=0.7228240966796875]
AirbornePositionMessage[timeStampNs=2471212800, icaoAddress=IcaoAddress[string=495299], altitude=10546.08, parity=0, x=0.6861114501953125, y=0.72503662109375]
AirbornePositionMessage[timeStampNs=2567937000, icaoAddress=IcaoAddress[string=4D2228], altitude=10972.800000000001, parity=0, x=0.6405563354492188, y=0.619598388671875]
AirbornePositionMessage[timeStampNs=2580798000, icaoAddress=IcaoAddress[string=39D300], altitude=11582.400000000001, parity=0, x=0.6239242553710938, y=0.6169815063476562]
AirbornePositionMessage[timeStampNs=2584585300, icaoAddress=IcaoAddress[string=4BCDE9], altitude=10972.800000000001, parity=1, x=0.7251510620117188, y=0.6266403198242188]
AirbornePositionMessage[timeStampNs=2662471400, icaoAddress=IcaoAddress[string=4D029F], altitude=4236.72, parity=0, x=0.7468185424804688, y=0.7338180541992188]
AirbornePositionMessage[timeStampNs=2729261700, icaoAddress=IcaoAddress[string=4B1A00], altitude=2788.92, parity=1, x=0.6782608032226562, y=0.5555496215820312]
AirbornePositionMessage[timeStampNs=2808517000, icaoAddress=IcaoAddress[string=4B17E5], altitude=1897.38, parity=1, x=0.650421142578125, y=0.5418548583984375]
AirbornePositionMessage[timeStampNs=2813102200, icaoAddress=IcaoAddress[string=3C6481], altitude=10370.82, parity=1, x=0.846771240234375, y=0.6130523681640625]
AirbornePositionMessage[timeStampNs=2906194400, icaoAddress=IcaoAddress[string=495299], altitude=10546.08, parity=0, x=0.6859970092773438, y=0.7249679565429688]
AirbornePositionMessage[timeStampNs=2911496600, icaoAddress=IcaoAddress[string=4D0221], altitude=822.96, parity=1, x=0.669097900390625, y=0.5681533813476562]
AirbornePositionMessage[timeStampNs=2912510900, icaoAddress=IcaoAddress[string=39CEAA], altitude=10363.2, parity=1, x=0.4866180419921875, y=0.5942764282226562]
AirbornePositionMessage[timeStampNs=2935911000, icaoAddress=IcaoAddress[string=A4F239], altitude=10363.2, parity=1, x=0.5797882080078125, y=0.35076141357421875]
AirbornePositionMessage[timeStampNs=3150286900, icaoAddress=IcaoAddress[string=4D2228], altitude=10972.800000000001, parity=1, x=0.6250686645507812, y=0.492706298828125]
AirbornePositionMessage[timeStampNs=3179279100, icaoAddress=IcaoAddress[string=4B1A00], altitude=2781.3, parity=1, x=0.6781997680664062, y=0.5554962158203125]
AirbornePositionMessage[timeStampNs=3322516000, icaoAddress=IcaoAddress[string=39CEAA], altitude=10363.2, parity=0, x=0.49875640869140625, y=0.7231597900390625]
AirbornePositionMessage[timeStampNs=3333067700, icaoAddress=IcaoAddress[string=3C6481], altitude=10370.82, parity=0, x=0.8680267333984375, y=0.7422332763671875]
AirbornePositionMessage[timeStampNs=3358467600, icaoAddress=IcaoAddress[string=4B17E1], altitude=4945.38, parity=0, x=0.6279525756835938, y=0.7650375366210938]
AirbornePositionMessage[timeStampNs=3428806700, icaoAddress=IcaoAddress[string=495299], altitude=10553.7, parity=0, x=0.6858596801757812, y=0.724884033203125]
AirbornePositionMessage[timeStampNs=3495274700, icaoAddress=IcaoAddress[string=4D0221], altitude=815.34, parity=0, x=0.685882568359375, y=0.6964950561523438]
AirbornePositionMessage[timeStampNs=3663721900, icaoAddress=IcaoAddress[string=440237], altitude=11277.6, parity=1, x=0.5326690673828125, y=0.5016860961914062]
AirbornePositionMessage[timeStampNs=3682884300, icaoAddress=IcaoAddress[string=4D2228], altitude=10972.800000000001, parity=0, x=0.6408233642578125, y=0.6198043823242188]
AirbornePositionMessage[timeStampNs=3708360900, icaoAddress=IcaoAddress[string=4B2964], altitude=4175.76, parity=1, x=0.7755661010742188, y=0.6391143798828125]
AirbornePositionMessage[timeStampNs=3719244300, icaoAddress=IcaoAddress[string=4B1A00], altitude=2781.3, parity=1, x=0.6781005859375, y=0.5554122924804688]
AirbornePositionMessage[timeStampNs=3732622800, icaoAddress=IcaoAddress[string=4951CE], altitude=6705.6, parity=0, x=0.8519668579101562, y=0.7913360595703125]
AirbornePositionMessage[timeStampNs=3756122600, icaoAddress=IcaoAddress[string=4CA2BF], altitude=5798.820000000001, parity=0, x=0.5659255981445312, y=0.7481460571289062]
AirbornePositionMessage[timeStampNs=3768086500, icaoAddress=IcaoAddress[string=3C6481], altitude=10370.82, parity=0, x=0.8680953979492188, y=0.7423477172851562]
AirbornePositionMessage[timeStampNs=3843907900, icaoAddress=IcaoAddress[string=495299], altitude=10553.7, parity=0, x=0.6857452392578125, y=0.7248077392578125]
AirbornePositionMessage[timeStampNs=3885921800, icaoAddress=IcaoAddress[string=A4F239], altitude=10363.2, parity=0, x=0.5936050415039062, y=0.475494384765625]
AirbornePositionMessage[timeStampNs=4011575200, icaoAddress=IcaoAddress[string=4D0221], altitude=815.34, parity=1, x=0.669219970703125, y=0.5682754516601562]
AirbornePositionMessage[timeStampNs=4220520200, icaoAddress=IcaoAddress[string=01024C], altitude=10980.42, parity=1, x=0.8081130981445312, y=0.555755615234375]
AirbornePositionMessage[timeStampNs=4222114100, icaoAddress=IcaoAddress[string=4D2228], altitude=10972.800000000001, parity=1, x=0.6253280639648438, y=0.49291229248046875]
AirbornePositionMessage[timeStampNs=4222824200, icaoAddress=IcaoAddress[string=4B2964], altitude=4175.76, parity=0, x=0.7950057983398438, y=0.7686233520507812]
AirbornePositionMessage[timeStampNs=4227579400, icaoAddress=IcaoAddress[string=39CEAA], altitude=10363.2, parity=1, x=0.486541748046875, y=0.5947341918945312]
AirbornePositionMessage[timeStampNs=4229259700, icaoAddress=IcaoAddress[string=4B1A00], altitude=2781.3, parity=0, x=0.694976806640625, y=0.6834030151367188]
AirbornePositionMessage[timeStampNs=4368912000, icaoAddress=IcaoAddress[string=495299], altitude=10553.7, parity=1, x=0.6688690185546875, y=0.5959701538085938]
AirbornePositionMessage[timeStampNs=4421279500, icaoAddress=IcaoAddress[string=4BCDE9], altitude=10972.800000000001, parity=1, x=0.7246170043945312, y=0.626922607421875]
AirbornePositionMessage[timeStampNs=4424072900, icaoAddress=IcaoAddress[string=3C6545], altitude=11590.02, parity=1, x=0.858551025390625, y=0.6821670532226562]
AirbornePositionMessage[timeStampNs=4475329800, icaoAddress=IcaoAddress[string=4D0221], altitude=807.72, parity=1, x=0.6692581176757812, y=0.5683212280273438]
AirbornePositionMessage[timeStampNs=4581047700, icaoAddress=IcaoAddress[string=4D029F], altitude=4229.1, parity=1, x=0.7282867431640625, y=0.6046066284179688]
AirbornePositionMessage[timeStampNs=4608801200, icaoAddress=IcaoAddress[string=4B17E5], altitude=1882.14, parity=1, x=0.6502151489257812, y=0.5420761108398438]
AirbornePositionMessage[timeStampNs=4669266500, icaoAddress=IcaoAddress[string=4B1A00], altitude=2773.6800000000003, parity=0, x=0.6949081420898438, y=0.683349609375]
AirbornePositionMessage[timeStampNs=4672806400, icaoAddress=IcaoAddress[string=4951CE], altitude=6705.6, parity=0, x=0.8521652221679688, y=0.79150390625]
AirbornePositionMessage[timeStampNs=4673064100, icaoAddress=IcaoAddress[string=3C6481], altitude=10370.82, parity=1, x=0.847076416015625, y=0.6135635375976562]
AirbornePositionMessage[timeStampNs=4722539900, icaoAddress=IcaoAddress[string=39CEAA], altitude=10363.2, parity=0, x=0.4986724853515625, y=0.7236251831054688]
AirbornePositionMessage[timeStampNs=4730733600, icaoAddress=IcaoAddress[string=4B2964], altitude=4175.76, parity=1, x=0.7756576538085938, y=0.6391830444335938]
AirbornePositionMessage[timeStampNs=4768552300, icaoAddress=IcaoAddress[string=4D2228], altitude=10972.800000000001, parity=1, x=0.625457763671875, y=0.493011474609375]
AirbornePositionMessage[timeStampNs=4825922400, icaoAddress=IcaoAddress[string=A4F239], altitude=10363.2, parity=0, x=0.5932998657226562, y=0.4756317138671875]
AirbornePositionMessage[timeStampNs=5021779700, icaoAddress=IcaoAddress[string=4D029F], altitude=4229.1, parity=0, x=0.7464065551757812, y=0.7334136962890625]
AirbornePositionMessage[timeStampNs=5123076500, icaoAddress=IcaoAddress[string=3C6481], altitude=10370.82, parity=1, x=0.8471450805664062, y=0.613677978515625]
AirbornePositionMessage[timeStampNs=5219108600, icaoAddress=IcaoAddress[string=4D2228], altitude=10972.800000000001, parity=1, x=0.6255645751953125, y=0.49309539794921875]
AirbornePositionMessage[timeStampNs=5250520700, icaoAddress=IcaoAddress[string=01024C], altitude=10980.42, parity=0, x=0.8280258178710938, y=0.68402099609375]
AirbornePositionMessage[timeStampNs=5259942500, icaoAddress=IcaoAddress[string=4B2964], altitude=4183.38, parity=0, x=0.7950973510742188, y=0.7686996459960938]
AirbornePositionMessage[timeStampNs=5277535800, icaoAddress=IcaoAddress[string=39CEAA], altitude=10363.2, parity=1, x=0.48648834228515625, y=0.595062255859375]
AirbornePositionMessage[timeStampNs=5374073500, icaoAddress=IcaoAddress[string=3C6545], altitude=11590.02, parity=0, x=0.880706787109375, y=0.8126373291015625]
AirbornePositionMessage[timeStampNs=5418420800, icaoAddress=IcaoAddress[string=4B17E1], altitude=4914.900000000001, parity=1, x=0.6129989624023438, y=0.6351394653320312]
AirbornePositionMessage[timeStampNs=5444719600, icaoAddress=IcaoAddress[string=4BB279], altitude=6111.240000000001, parity=0, x=0.7886962890625, y=0.6512298583984375]
AirbornePositionMessage[timeStampNs=5472936100, icaoAddress=IcaoAddress[string=4B17E5], altitude=1882.14, parity=0, x=0.6663818359375, y=0.6700210571289062]
AirbornePositionMessage[timeStampNs=5619249800, icaoAddress=IcaoAddress[string=4B1A00], altitude=2773.6800000000003, parity=0, x=0.6947479248046875, y=0.6832199096679688]
AirbornePositionMessage[timeStampNs=5684907400, icaoAddress=IcaoAddress[string=4CA2BF], altitude=5798.820000000001, parity=0, x=0.5658340454101562, y=0.7475662231445312]
AirbornePositionMessage[timeStampNs=5688356200, icaoAddress=IcaoAddress[string=3C6481], altitude=10370.82, parity=1, x=0.8472442626953125, y=0.6138458251953125]
AirbornePositionMessage[timeStampNs=5715422700, icaoAddress=IcaoAddress[string=4B2964], altitude=4183.38, parity=1, x=0.7757492065429688, y=0.6392593383789062]
AirbornePositionMessage[timeStampNs=5736587400, icaoAddress=IcaoAddress[string=495299], altitude=10553.7, parity=0, x=0.6851959228515625, y=0.7244644165039062]
AirbornePositionMessage[timeStampNs=5742208300, icaoAddress=IcaoAddress[string=4D2228], altitude=10972.800000000001, parity=0, x=0.641326904296875, y=0.6202011108398438]
AirbornePositionMessage[timeStampNs=5765929900, icaoAddress=IcaoAddress[string=A4F239], altitude=10363.2, parity=1, x=0.578857421875, y=0.3511810302734375]
AirbornePositionMessage[timeStampNs=5998271000, icaoAddress=IcaoAddress[string=4D029F], altitude=4229.1, parity=0, x=0.7462387084960938, y=0.733245849609375]
AirbornePositionMessage[timeStampNs=6025450100, icaoAddress=IcaoAddress[string=4D0221], altitude=800.1, parity=1, x=0.6694183349609375, y=0.5684738159179688]
AirbornePositionMessage[timeStampNs=6049248700, icaoAddress=IcaoAddress[string=4B1A00], altitude=2766.06, parity=0, x=0.6946792602539062, y=0.68316650390625]
AirbornePositionMessage[timeStampNs=6103072700, icaoAddress=IcaoAddress[string=3C6481], altitude=10363.2, parity=1, x=0.8473281860351562, y=0.6139907836914062]
AirbornePositionMessage[timeStampNs=6242739900, icaoAddress=IcaoAddress[string=4CA2BF], altitude=5798.820000000001, parity=0, x=0.5658187866210938, y=0.747406005859375]
AirbornePositionMessage[timeStampNs=6252823600, icaoAddress=IcaoAddress[string=4B2964], altitude=4191.0, parity=0, x=0.7951812744140625, y=0.7687759399414062]
AirbornePositionMessage[timeStampNs=6310512200, icaoAddress=IcaoAddress[string=01024C], altitude=10980.42, parity=1, x=0.8075408935546875, y=0.55615234375]
AirbornePositionMessage[timeStampNs=6316314200, icaoAddress=IcaoAddress[string=4BCDE9], altitude=10972.800000000001, parity=1, x=0.7240676879882812, y=0.6272125244140625]
AirbornePositionMessage[timeStampNs=6408458600, icaoAddress=IcaoAddress[string=4B17E1], altitude=4907.280000000001, parity=1, x=0.6131744384765625, y=0.6349105834960938]
AirbornePositionMessage[timeStampNs=6432548700, icaoAddress=IcaoAddress[string=39CEAA], altitude=10363.2, parity=1, x=0.486419677734375, y=0.5954513549804688]
AirbornePositionMessage[timeStampNs=6503498300, icaoAddress=IcaoAddress[string=4D0221], altitude=792.48, parity=1, x=0.669464111328125, y=0.5685195922851562]
AirbornePositionMessage[timeStampNs=6540584200, icaoAddress=IcaoAddress[string=4D029F], altitude=4221.4800000000005, parity=1, x=0.7279586791992188, y=0.6042709350585938]
AirbornePositionMessage[timeStampNs=6586640700, icaoAddress=IcaoAddress[string=4B1A1E], altitude=9044.94, parity=0, x=0.7992095947265625, y=0.574066162109375]
AirbornePositionMessage[timeStampNs=6649117100, icaoAddress=IcaoAddress[string=4B17E5], altitude=1874.52, parity=0, x=0.666259765625, y=0.6701812744140625]
AirbornePositionMessage[timeStampNs=6653130500, icaoAddress=IcaoAddress[string=3C6481], altitude=10370.82, parity=0, x=0.8685989379882812, y=0.7431869506835938]
AirbornePositionMessage[timeStampNs=6711750100, icaoAddress=IcaoAddress[string=440237], altitude=11277.6, parity=0, x=0.5451507568359375, y=0.6282424926757812]
AirbornePositionMessage[timeStampNs=6733320500, icaoAddress=IcaoAddress[string=4CA2BF], altitude=5798.820000000001, parity=0, x=0.5657958984375, y=0.7472610473632812]
AirbornePositionMessage[timeStampNs=6739433700, icaoAddress=IcaoAddress[string=4B2964], altitude=4191.0, parity=1, x=0.7758331298828125, y=0.6393280029296875]
AirbornePositionMessage[timeStampNs=6857664800, icaoAddress=IcaoAddress[string=39CEAA], altitude=10363.2, parity=1, x=0.48639678955078125, y=0.5955810546875]
AirbornePositionMessage[timeStampNs=6881606700, icaoAddress=IcaoAddress[string=495299], altitude=10561.32, parity=1, x=0.6682052612304688, y=0.595550537109375]
AirbornePositionMessage[timeStampNs=7013032200, icaoAddress=IcaoAddress[string=4D2228], altitude=10972.800000000001, parity=0, x=0.6416397094726562, y=0.6204452514648438]
AirbornePositionMessage[timeStampNs=7019252900, icaoAddress=IcaoAddress[string=4B1A00], altitude=2766.06, parity=0, x=0.69451904296875, y=0.6830368041992188]
AirbornePositionMessage[timeStampNs=7059959800, icaoAddress=IcaoAddress[string=4D029F], altitude=4221.4800000000005, parity=0, x=0.7460708618164062, y=0.7330703735351562]
AirbornePositionMessage[timeStampNs=7060665700, icaoAddress=IcaoAddress[string=4D0221], altitude=792.48, parity=0, x=0.6862640380859375, y=0.6968612670898438]
AirbornePositionMessage[timeStampNs=7208021100, icaoAddress=IcaoAddress[string=4B2964], altitude=4191.0, parity=0, x=0.7952728271484375, y=0.7688446044921875]
AirbornePositionMessage[timeStampNs=7228828000, icaoAddress=IcaoAddress[string=4BCDE9], altitude=10972.800000000001, parity=1, x=0.7238082885742188, y=0.627349853515625]
AirbornePositionMessage[timeStampNs=7440752300, icaoAddress=IcaoAddress[string=4D2228], altitude=10972.800000000001, parity=1, x=0.6260910034179688, y=0.4935150146484375]
AirbornePositionMessage[timeStampNs=7463177400, icaoAddress=IcaoAddress[string=4D0221], altitude=784.86, parity=0, x=0.6863021850585938, y=0.6968994140625]
 */