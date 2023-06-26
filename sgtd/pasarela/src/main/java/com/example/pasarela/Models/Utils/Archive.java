package com.example.pasarela.Models.Utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignature;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.DigestAlgorithms;
import com.itextpdf.text.pdf.security.ExternalBlankSignatureContainer;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.ExternalSignatureContainer;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.PdfPKCS7;
import com.itextpdf.text.pdf.security.MakeSignature.CryptoStandard;
import com.itextpdf.text.pdf.security.PrivateKeySignature;

public class Archive {
	private final Logger log = LoggerFactory.getLogger(getClass());

	 
	 public void sign(String keystore, char[] password,int level, String src,String dest)
	 throws GeneralSecurityException, IOException, DocumentException {
	 
	 
	 
	  KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
	 ks.load(new FileInputStream(keystore), password);
	  String alias = (String) ks.aliases().nextElement();
	  PrivateKey pk = (PrivateKey) ks.getKey(alias, password);
	  Certificate[] chain = ks.getCertificateChain(alias);
	  // Creating the reader and the stamper
	  PdfReader reader = new PdfReader(src);
	  FileOutputStream os = new FileOutputStream(dest);
	  PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0', null,
	 true);
	  // Creating the appearance
	  PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
	  // appearance.setVisibleSignature(name);
	  appearance.setReason("USIC Unidad de Sistemas de Informacion y Comunicacion"
	  );
	  appearance.setLocation("Cobija - Pando");
	  appearance.setCertificationLevel(level);
	  // Creating the signature
	  ExternalSignature pks = new PrivateKeySignature(pk, "SHA-256", "BC");
	  ExternalDigest digest = new BouncyCastleDigest();
	  MakeSignature.signDetached(appearance, digest, pks, chain, null, null, null,
	  0, CryptoStandard.CMS);
	  
	  os.close();
	  stamper.close();
	  reader.close();
	  }
	  

	
	

	public String getMD5(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			String hashtext = number.toString(16);

			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public String plantilla(String archivo, String salida, String plantilla, String codigo)
			throws IOException, DocumentException {
		// Leo el contenido de mi PDF base
		PdfReader reader = new PdfReader(archivo);
		// Creo el stamper especificando el contenido base y el archivo de salida
		PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(salida));
		// Obtengo el contenido del pdf. Si utilizo getUnderContent lo que agregue
		// aparecera debajo del contenido de mi PDF original
		// si utilizo getOverContent los elementos agregados apareceran encima del
		// contenido de mi PDF original
		PdfContentByte cb = stamp.getUnderContent(1);
		// Creo una imagen para agregarla y le pongo propiedades de posicion y escala
		Image image = Image.getInstance(plantilla);
		image.setAbsolutePosition(0, 0);
		image.scalePercent(48);
		// Agrego una imagen, la cual ya tiene las propiedades de posicion
		cb.addImage(image);
		// Cierro el stamper y se crea el archivo.
		stamp.close();
		reader.close();

		return codigo + ".pdf";
	}

}
