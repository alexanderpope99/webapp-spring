const cod_boala = [
  '01-Boala obisnuita si accidente in afara muncii',
  '02-Accident in timpul deplasarii la/de la locul de munca',
  '03-Accident de munca',
  '04-Boala profesionala',
  '05-Boala infectocontagioasa din grupa A',
  '51-Boala infectocontagioasa pentru care se instituie masura izolarii',
  '06-Urgenta medico-chirurgicala',
  '07-Carantina',
  '08-Sarcina si lehuzie (maternitate)',
  '09-Ingrijire copil bolnav in varsta de pana la 7 ani sau copil cu handicap, pentru afectiuni intercurente, pana la implinirea varstei de 18 ani 85%',
  'Ingrijire copil bolnav cu afectiuni grave, in varsta de pana la 16 ani',
  '10-Reducerea duratei cu ¼ a duratei normale a timpului de lucru',
  '11-Trecerea temporara in alt loc de munca',
  '12-Tuberculoza',
  '13-Boala cardiovasculara',
  '14-Cancer, HIV, Neoplazii, SIDA',
  '15-Risc maternal',
];

const cod_urgenta = [
  '',
  '1. arsurile',
  '2. anemiile severe, cu complicatii cardio-vasculare',
  '3. accidentele tromboembolice la pacientii cu tromboflebitã ereditarã',
  '4. accidente toxice hematologice si viscerale, secundare tratamentelor cu citostatice',
  '5. accidentele survenite dupã terapia de substitutie cu produse sanguine',
  '6. accidente cardiace sau vasculare dupã cateterism cardiac',
  '7. angioaccesul pentru dializa extra-renalã',
  '8. artrita gutoasã acutã',
  '9. angiocolecistita acutã',
  '10. artritele reactive în puseu acut',
  '11. artritele septice',
  '12. abdomenul acut, medico-chirurgical',
  '13. abcesul pulmonar',
  '14. anuria',
  '15. astmul bronsic în crizã',
  '16. accidentele vasculare cerebrale (infarctul cerebral, hemoragia cerebralã, hemoragia subarahnoidianã, atacul ischemic tranzitoriu)',
  '17. atacul migrenos',
  '18. atacul cerebral',
  '19. avortul complicat septic sau cu soc hemoragic',
  '29. atacul acut de glaucom',
  '21. apoplexia utero-placentarã',
  '22. abcesul sau flegmonul cu stare septicã',
  '23. bolile cardiace congenitale ale nou-nãscutului si copilului mic',
  '24. bronhopneumonia, pneumopatiile acute virale si bacteriene',
  '25. BPOC cu insuficientã respiratorie severã',
  '26. blocurile atrio-ventriculare simptomatice neonatale',
  '27. bolile medico-chirurgicale specifice perinatale',
  '28. criza epilepticã si starea de rãu epileptic',
  '29. criza miastenicã',
  '30. cetoacidoza la pacientii cu diabet zaharat tip 1, tip 2, si în diabetul gestational',
  '31. chistul hidatic evacuat',
  '32. corpii strãini (nazali, faringieni, laringieni, traheo-bronhici, esofagieni, auriculari cu lezarea pãrtilor conductului auditiv extern sau a timpanului)',
  '33. conjunctivita acutã viralã sau microbianã',
  '34. corpi strãini penetranti sau nepenetranti în globul ocular',
  '35. colica renalã, biliarã, abdominalã',
  '36. colecistita acutã',
  '37. colita ischemicã',
  '38. complicatiile sarcinii',
  '39. complicatiile litiazei reno-uretrale si vezicale',
  '40. criza de lumbago',
  '41. coma hipofizarã',
  '42. criza tireotoxicã',
  '43. coma mixedematoasã',
  '44. coma hiperglicemicã',
  '45. comele diabetice (cetoacidozã, hiperosmolarã si lacticã)',
  '46. come de alte etiologii',
  '47. criza addisonianã',
  '48. criza de hemolizã acutã extra- si intravascularã',
  '49. complicatiile hemoragice dupã interventii cardiovasculere',
  '50. complicatiile septice dupã interventiile cardiovasculare',
  '51. carditele acuta (endocardite, miocardite si pericardite)',
  '52. crizele severe de cianozã, hipoxie, acidoze în cardio-vascular',
  '53. convulsiile, starea de rãu convulsiv',
  '54. disgravidia precoce-forma severã',
  '55. disgravidia tardivã',
  '56. dermatomiozitele active',
  '57. diabetul zaharat tip 1 nou-depistat',
  '58. diabetul gestational nou-depistat',
  '59. delirul',
  '60. decolarea de retinã',
  '61. dispneea faringianã, laringianã, trahealã înaltã',
  '62. disfagia totalã de cauzã bucofaringianã sau esofagianã',
  '63. disectia de aortã, anevrismul disecant al aortae si alte leziuni parietale cu manifestãri acute',
  '64. disfunctiile acute de proteze cardiace',
  '65. edemul pulmonar acut',
  '66. edemul pulmonar acut de cauzã cardiovascularã',
  '67. encefalopatia hepaticã',
  '68. eritrocitoze asociate cu fenomene de sludge',
  '69. embolia pulmonarã',
  '70. eclampsia si preeclampsia',
  '71. flebita si tromboflebita profundã extensivã',
  '72. gamapatiile monoclonale cu sindrom de hipervâscozitate',
  '73. hemoptizia',
  '74. hematuria',
  '75. hemoragiile nazale (epistaxisul major, epistaxisul minor, repetat cu anemie secundarã), faringiene, laringiene, traheale (posttraumatice, tumorale) si hemoragii exteriorizate prin conductul auditiv extern',
  '76. hemoragia digestivã superioarã (hematemeza)',
  '77. hemoragia digestivã inferioarã',
  '78. hemoragiile genitale',
  '79. hemoragia in vitros si retinopatia diabeticã preproliferativã si proliferativã (cazuri noi)',
  '80. hipertrigliceridemiile severe (> 1000 mg/ l)',
  '81. hepatita acutã toxicã/ medicamentoasã',
  '82. hipercalcemia',
  '83. hipertensiunea aretrialã paroxisticã',
  '84. insuficientele cardiorespiratorii acute',
  '85. insuficienta respiratorie la obezitate cu risc crescut (IMC > 40)',
  '86. infectiile acute ale cãilor respiratorii superioare, cu alterarea stãrii generale',
  '87. infectii acute ale mâinii',
  '88. infectii acute rinosinusale, otice, faringiene, perifaringiene si cervico-mediastinale',
  '89. infectiile si inflamatiile acute ale anexelor globului ocular',
  '90. infectiile acute osoase',
  '91. infectiile acute urinare',
  '92. insuficienta renalã acutã',
  '93. insuficienta corticosuprarenalã acutã  (iatrogenã sau de alta cauze)',
  '94. ischemia visceralã (renalã, entero-mezentericã, hepaticã, splenicã)',
  '95. intoxicatiile acute voluntare si involuntare ',
  '96. keratita viralã sau microbianã',
  '97. lovirea sau alte acte de violentã ce au avut ca rezultat vãtãmarea corporalã, conform art. 180, 181, 182, 184 si 211 (tâlhãria) din Codul penal',
  '  98. lombosciatica',
  '99. lupusul eritematos sistematic activ, cu manifestãri renale, miocardice, seroase, vasculitice)',
  '100. leucemia acutã în faza de evolutie initialã sau de recãdere',
  '101. leucemiile cronice cu hiperleucocitozã si sindrom de leucostazã',
  '102. limfoame maligne cu mase ganglionare compresive',
  '103. nevralgia cervicobrahialã',
  '104. ocluziile intestinale',
  '105. ocluziile vasculare retiniene',
  '106 .pusee acute de sclerozã în plãgi',
  '107. paraliziile periodice diskaliemice',
  '108. pleureziile acute virale si bacteriene',
  '109. pneumotoraxul neterapeutic',
  '110. polimiozite acute',
  '111. pneumopatia de aspiratie',
  '112. plãgi si traumatisme cardiopericardice si vasculare',
  '113. plãgi complicate',
  '114. plãgi ale globului ocular si organelor anexe',
  '115. parodontita apicalã acutã',
  '116. pulpita acutã',
  '117. poliradiculonevrita acutã',
  '118. paralizia perifericã de nerv facial',
  '119. penetratia si perforatia ulcerelor',
  '120. pancreatitele acute',
  '121. peritonita',
  '122. patologia inflamatorie acutã a uterului si anexelor',
  '123. periartrita scapulohumeralã –umarul acut hiperalgic',
  '124. poliartrita reumatoidã activã în puseu acut',
  '125. pericardita acutã cu tamponadã cardiacã',
  '126. politraumatisme cu leziuni importante',
  '127. reumatismul articular acut',
  '128. rectocolita hemoragicã - puseu acut – megacolonul toxic',
  '129. reactiile alergice',
  '130. reactiile alergice oculare',
  '131. reactiile adverse, severe la terapia hipoglicemiantã oralã (cutanate si digestive)',
  '132. retentia acutã de urinã',
  '133. stãrile comatoase de cauzã neurologicã',
  '134. stãrile comfuzionale acute în afectiunile sistemice, inclusiv în bolile nutritionale si metabolice ale sistemului nervos',
  '135. sindroamele cefalgice acute',
  '136. sindroamele septice',
  '137. sindromul de agitatie psihomo-torie',
  '138. sindromul psihopatic acut',
  '139. sindromul catatonic',
  '140. sindromul discomportamental violent',
  '141. sarcina extrauterinã',
  '142. sincopa',
  '143. sindromul de ischemie perifericã acutã',
  '144. supuratiile acute si cele cronice, reactualizate',
  '145. sindroamele vestibulare în crizã',
  '146. surditatea brusc instalatã sau brusc agravatã',
  '147. sângerãrile uterine disfunctionale',
  '148. sindromul de debit cardiac scãzut prin colmatarea valvulelor',
  '149. socul cardiogen si alte stãri de debit cardiac scãzut',
  '150. sindroamele coronariene acute (IMA, angina instabilã)',
  '151. stãrile de instabilitate hemodinamicã sau aritmia acutã survenitã la bolnavii cu cardiopatii cronice, care le pot pune în pericol viata',
  '152. sindroame hemoragice cu manifestãri clinice ce pun în pericol viata',
  '153. sindromul febril',
  '154. sindromul de hiperuricemie',
  '155. traumatismele musculare neuro-vasculare, osteoarticulare, tegumentare si viscerale recente',
  '156. traumatismele craniene si ale mãduvei spinãrii',
  '157. traumatismele toracice cu insuficientã respiratorie',
  '158. tulburarea depresivã cu risc suicidar',
  '159. tulburarea anxioasã paroxisticã (atacul de panicã )',
  '160. tulburarea acutã de stres',
  '161. traumatismele aparatului urinar',
  '162. traumatismele aparatului locomotor (fracturi, luxatii, entorse, rupturi de tendoane, rupturi musculare), cu exceptia celor determinate de alcoolism',
  '163. tumorile maligne sângerânde',
  '164. tromboflebita orbito-cavernoasã',
  '165. traumatismele orbito-oculare cu complicatii',
  '166. traumatismele abdominale',
  '167. tumorile neuroendocrine cu eliberãri paroxistice de insulinã, VIP, serotoninã, glucagon',
  '168. trombembolismul pulmonar acut',
  '169. tulburãrile acute paroxistice de ritm supraventriculare',
  '170.tahicardiile ventriculare',
  '171.tromboflebita acutã proximalã severã',
  '172. trombocitozele complicate cu eritromelalgie si cu alte fenomene de constrictie a microcirculatiei',
  '173. urgentele hipertensive (encefalopatia hipertensivã, criza hipertensivã, hemoragia cerebro-meningee, eclampsia)',
  '174. uveita opticã, cu exceptia celei alcoolo-tabagice',
  '175. ulcerul gastro-duodenal în puseu acut',
];

const cod_boala_infect = [
  '',
  '1.amibiaza (dizenteria amibianã )',
  '2.antraxul',
  '3.bruceloza',
  '4.difteria',
  '5.febra butunoasã',
  '6.febra galbenã',
  '7.febrele paratifoide A;B;C',
  '8.febra Q',
  '9.febra recurentã',
  '10.febra tifoidã',
  '11.filarioza , dracunculoza',
  '12.hepatita viralã',
  '13.holera',
  '14.infectia gonococicã',
  '15.leishmaniozele',
  '16.lepra',
  '17.leptospiroza',
  '18.limfogranulomatoza inghinalã benignã',
  '19.malaria',
  '20.meningita cerebrospinalã epidemicã',
  '21.morva, meioidoza',
  '22.pesta',
  '23.poliomielita si alte neuroviroze paralitice',
  '24.psitacoza- ornitoza',
  '25.rabia',
  '26.RAA',
  '27.scarlatina',
  '28.sifilisul',
  '29.sancrul moale',
  '30.tetanosul',
  '31.tifosul exantematic',
  '32.tuberculoza (toate formele si localizãrile)',
  '33.tularemia',
  '34.tusea convulsivã',
  '35.complicatiile postvaccinale',
  '36.infectia HIV',
];

function getProcente(cod) {
  cod = cod.substring(0, 2);
  switch (cod) {
    case '01':
      return ['75'];
    case '02':
      return ['80', '100'];
    case '03':
      return ['80', '100'];
    case '04':
      return ['80', '100'];
    case '05':
      return ['100'];
    case '51':
      return ['100'];
    case '06':
      return ['100'];
    case '07':
      return ['100'];
    case '08':
      return ['85'];
    case '09':
      return ['85'];
    case '10':
      return ['25'];
    case '11':
      return ['25'];
    case '12':
      return ['100'];
    case '13':
      return ['75'];
    case '14':
      return ['100'];
    case '15':
      return ['75'];

    default:
      return ['100'];
  }
}

function getZileFirma(dela, panala, cod, sarbatori) {
  if (dela > panala) return '** Dată început > Dată sfârșit **';

  let nr_zile = (panala.getTime() - dela.getTime()) / (1000 * 3600 * 24) + 1;
  let nr_zile_lucratoare = nr_zile - getFreeDays(dela, panala, sarbatori);
  let zilefirma = 0;
  let zilefnuass = 0;
  let zilefaambp = 0;
  if (
    cod === '01' ||
    cod === '05' ||
    cod === '06' ||
    cod === '12' ||
    cod === '13' ||
    cod === '14'
  ) {
    if (nr_zile_lucratoare <= 5) return [nr_zile_lucratoare, 0, 0];
    zilefirma = 5 - getFreeDays(dela, addDays(dela, 4), sarbatori);
    zilefnuass = nr_zile - 5 - getFreeDays(addDays(dela, 4), panala, sarbatori);
  }
  if (cod === '02' || cod === '03' || cod === '04') {
    if (nr_zile_lucratoare <= 3) return [nr_zile_lucratoare, 0, 0];
    zilefirma = 3 - getFreeDays(dela, addDays(dela, 2), sarbatori);
    zilefnuass = 0;
    zilefaambp = nr_zile - zilefirma - getFreeDays(addDays(dela, 2), panala, sarbatori);
  }
  if (
    cod === '51' ||
    cod === '07' ||
    cod === '08' ||
    cod === '09' ||
    cod === '91' ||
    cod === '10' ||
    cod === '11' ||
    cod === '15'
  ) {
    zilefirma = 0;
    zilefnuass = nr_zile - getFreeDays(dela, addDays(dela, nr_zile), sarbatori);
    zilefaambp = 0;
  }
  return [zilefirma, zilefnuass, zilefaambp];
}

function getFreeDays(startDate, endDate, holidays) {
  let rv = countWeekendDays(startDate, endDate) + countHolidays(startDate, endDate, holidays);
  // console.log('zile libere:', rv);
  return rv;
}

function addDays(date, days) {
  var result = new Date(date);
  result.setDate(result.getDate() + days);
  return result;
}

function countWeekendDays(d0, d1) {
  var ndays = 1 + Math.round((d1.getTime() - d0.getTime()) / (24 * 3600 * 1000));
  var nsaturdays = Math.floor((d0.getDay() + ndays) / 7);
  // eslint-disable-next-line eqeqeq
  return 2 * nsaturdays + (d0.getDay() == 0) - (d1.getDay() == 6);
}

function countHolidays(startDate, endDate, holidays) {
  if (!holidays) return 0;
  var nholidays = 0;
  // eslint-disable-next-line array-callback-return
  holidays.map((holiday) => {
    let dela = Date.parse(holiday.dela);
    let panala = Date.parse(holiday.panala);

    // concediul nu include sarbatoarea
    if (panala < startDate || endDate < dela) {
      return 0;
    }
    // concediul include sarbatoarea in totalitate
    else if (startDate <= dela && panala <= endDate) {
      // console.log('concediul include sarbatoarea in totalitate');
      // console.log('se suprapun:', ((panala - dela) / (24 * 3600 * 1000) + 1), 'zile de sarbatoare');
      // console.log('din care', countWeekendDays(new Date(panala), new Date(dela + (24 * 3600 * 1000))), 'in weekend');
      nholidays +=
        (panala - dela) / (24 * 3600 * 1000) +
        1 -
        countWeekendDays(new Date(panala), new Date(dela + 24 * 3600 * 1000));
    }
    // sarbatoarea se termina in concediu, dar nu incepe in el
    else if (dela < startDate && panala <= endDate) {
      // console.log('sarbatoarea se termina in concediu, dar nu incepe in el');
      // console.log('se suprapun:' ((panala - startDate) / (24 * 3600 * 1000) + 1), 'zile');
      nholidays +=
        (panala - startDate) / (24 * 3600 * 1000) +
        1 -
        countWeekendDays(new Date(panala), addDays(startDate, 1));
    }
    // sarbatoarea incepe in concediu, dar nu se termina in el
    else if (startDate <= dela && endDate < panala) {
      // console.log('sarbatoarea incepe in concediu, dar nu se termina in el');
      // console.log('se suprapun:' ((endDate - dela)  / (24 * 3600 * 1000) + 1), 'zile');
      nholidays +=
        (endDate - dela) / (24 * 3600 * 1000) +
        1 -
        countWeekendDays(endDate, new Date(dela + 24 * 3600 * 1000));
    }
  });
  return nholidays;
}

export {
  cod_boala,
  cod_urgenta,
  cod_boala_infect,
  getProcente,
  getZileFirma,
  countWeekendDays,
  countHolidays,
};
