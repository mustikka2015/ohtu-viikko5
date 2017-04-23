package ohtu.verkkokauppa;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import ohtu.verkkokauppa.Kauppa;
import ohtu.verkkokauppa.Pankki;
import ohtu.verkkokauppa.PankkiInt;
import ohtu.verkkokauppa.Tuote;
import ohtu.verkkokauppa.Varasto;
import ohtu.verkkokauppa.VarastoInt;
import ohtu.verkkokauppa.Viitegeneraattori;
import ohtu.verkkokauppa.ViitegeneraattoriInt;
import static org.mockito.Mockito.*;
import org.junit.*;

/**
 *
 * @author Iisa
 */
public class KauppaTest {

    Kauppa kauppa;

    @Test
    public void ostoksenPaatyttyaPankinMetodiaTilisiirtoKutsutaan() {

        // luodaan kolme mock-olioa
        Varasto varasto = mock(Varasto.class);
        Pankki pankki = mock(Pankki.class);
        Viitegeneraattori viite = mock(Viitegeneraattori.class);

        when(viite.uusi()).thenReturn(42);
        // määritellään että tuote numero 1 on maito jonka hinta on 5 ja saldo 10
        when(varasto.saldo(1)).thenReturn(10);
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));

        // injektoidaan ne kaupalle normaalien olioiden tapaan
        kauppa = new Kauppa(varasto, pankki, viite);

//        // tehdään ostokset
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);     // ostetaan tuotetta numero 1 eli maitoa
        kauppa.tilimaksu("pekka", "12345");

        verify(pankki).tilisiirto(eq("pekka"), eq(42), eq("12345"), eq("33333-44455"), eq(5));

    }

    @Test
    public void ostoksenPaaytyttyaPankinMetodiaTilisiirtoKutsutaan2eriTuotetta() {

        Varasto varasto = mock(Varasto.class);
        Pankki pankki = mock(Pankki.class);
        Viitegeneraattori viite = mock(Viitegeneraattori.class);

        when(viite.uusi()).thenReturn(45);

        when(varasto.saldo(1)).thenReturn(10);
        when(varasto.saldo(2)).thenReturn(15);
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));
        when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "leipa", 3));

        kauppa = new Kauppa(varasto, pankki, viite);

        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.lisaaKoriin(2);
        kauppa.tilimaksu("Leena", "56789");

        verify(pankki).tilisiirto(eq("Leena"), eq(45), eq("56789"), eq("33333-44455"), eq(8));

    }

    @Test
    public void ostoksenPaaytyttyaPankinMetodiaTilisiirtoKutsutaan2samaaTuotetta() {

        Varasto varasto = mock(Varasto.class);
        Pankki pankki = mock(Pankki.class);
        Viitegeneraattori viite = mock(Viitegeneraattori.class);

        when(viite.uusi()).thenReturn(56);

        when(varasto.saldo(1)).thenReturn(10);
        
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));

        kauppa = new Kauppa(varasto, pankki, viite);

        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.lisaaKoriin(1);
        kauppa.tilimaksu("Viivi", "54321");

        verify(pankki).tilisiirto(eq("Viivi"), eq(56), eq("54321"), eq("33333-44455"), eq(10));

    }
    
     @Test
    public void ostoksenPaaytyttyaPankinMetodiaTilisiirtoKutsutaan2eriTuotettaToinenLoppu() {

        Varasto varasto = mock(Varasto.class);
        Pankki pankki = mock(Pankki.class);
        Viitegeneraattori viite = mock(Viitegeneraattori.class);

        when(viite.uusi()).thenReturn(45);

        when(varasto.saldo(1)).thenReturn(0);
        when(varasto.saldo(2)).thenReturn(15);
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));
        when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "leipa", 3));

        kauppa = new Kauppa(varasto, pankki, viite);

        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.lisaaKoriin(2);
        kauppa.tilimaksu("Leena", "56789");

        verify(pankki).tilisiirto(eq("Leena"), eq(45), eq("56789"), eq("33333-44455"), eq(3));

    }
    
    @Test
    public void ostoskoriTyhjeneeTest() {

        Varasto varasto = mock(Varasto.class);
        Pankki pankki = mock(Pankki.class);
        Viitegeneraattori viite = mock(Viitegeneraattori.class);

        when(viite.uusi()).thenReturn(45);

        when(varasto.saldo(1)).thenReturn(10);
        when(varasto.saldo(2)).thenReturn(15);
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));
        when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "leipa", 3));

        kauppa = new Kauppa(varasto, pankki, viite);

        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.lisaaKoriin(2);
        kauppa.tilimaksu("Leena", "56789");
        
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(2);
        kauppa.lisaaKoriin(2);
        kauppa.tilimaksu("Siiri", "12345");

        verify(pankki).tilisiirto(eq("Siiri"), eq(45), eq("12345"), eq("33333-44455"), eq(6));

    }
    
    @Test
    public void viitenumeroUusiTest() {

        Varasto varasto = mock(Varasto.class);
        Pankki pankki = mock(Pankki.class);
        Viitegeneraattori viite = mock(Viitegeneraattori.class);


        when(varasto.saldo(1)).thenReturn(10);
        when(varasto.saldo(2)).thenReturn(15);
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));
        when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "leipa", 3));

        kauppa = new Kauppa(varasto, pankki, viite);

        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.lisaaKoriin(2);
        kauppa.tilimaksu("Leena", "56789");
        
        verify(viite, times(1)).uusi();
        
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(2);
        kauppa.lisaaKoriin(2);
        kauppa.tilimaksu("Siiri", "12345");

        verify(viite, times(2)).uusi();
        
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.lisaaKoriin(1);
        kauppa.tilimaksu("Teemu", "54345");
        verify(viite, times(3)).uusi();

    }
    
    @Test
    public void viitenumeroUusiTest2() {

        Varasto varasto = mock(Varasto.class);
        Pankki pankki = mock(Pankki.class);
        Viitegeneraattori viite = mock(Viitegeneraattori.class);

        when(viite.uusi()).thenReturn(45);

        when(varasto.saldo(1)).thenReturn(10);
        when(varasto.saldo(2)).thenReturn(15);
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));
        when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "leipa", 3));

        kauppa = new Kauppa(varasto, pankki, viite);

        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.lisaaKoriin(2);
        kauppa.tilimaksu("Leena", "56789");
        
        verify(viite, times(1)).uusi();
        verify(pankki).tilisiirto(eq("Leena"), eq(45), eq("56789"), eq("33333-44455"), eq(8));
        
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(2);
        kauppa.lisaaKoriin(2);
        kauppa.tilimaksu("Siiri", "12345");
        verify(pankki).tilisiirto(eq("Siiri"), eq(45), eq("12345"), eq("33333-44455"), eq(6));

        verify(viite, times(2)).uusi();
        
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.lisaaKoriin(1);
        kauppa.tilimaksu("Teemu", "54345");
        verify(viite, times(3)).uusi();
        verify(pankki).tilisiirto(eq("Teemu"), eq(45), eq("54345"), eq("33333-44455"), eq(10));
    }
    
    @Test
    public void palauttaaVarastoonTest() {

        // luodaan kolme mock-olioa
        Varasto varasto = mock(Varasto.class);
        Pankki pankki = mock(Pankki.class);
        Viitegeneraattori viite = mock(Viitegeneraattori.class);

        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));

        // injektoidaan ne kaupalle normaalien olioiden tapaan
        kauppa = new Kauppa(varasto, pankki, viite);
        
        kauppa.lisaaKoriin(1);
        kauppa.lisaaKoriin(1);
        kauppa.poistaKorista(1);
       
        verify(varasto).haeTuote(eq(1));
        Tuote t = varasto.haeTuote(1);
        verify(varasto).palautaVarastoon(t);
        
        

    }

    
}

