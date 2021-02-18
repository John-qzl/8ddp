package com.cssrc.ibms.core.webseal.pdf.sign;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.List;

import javax.crypto.Cipher;

import com.cssrc.ibms.api.report.model.IbmsSign;
import com.cssrc.ibms.core.webseal.Cert.KeyStoreFactory;
import com.cssrc.ibms.core.webseal.pdf.KeyExecute;
import com.cssrc.ibms.core.webseal.pdf.KeyPosition;
import com.cssrc.ibms.core.webseal.pdf.PdfPositionEngine;
import com.cssrc.ibms.core.webseal.pdf.intf.AbstractIbmsSeal;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfSignatureAppearance.RenderingMode;
import com.itextpdf.text.pdf.PdfStamper;

/** 
* @ClassName: PdfSignature 
* @Description: pdf文件数字加密签名
* @author zxg 
* @date 2016年12月5日 下午3:00:34 
*  
*/
public class PdfSignature extends AbstractIbmsSeal {
	private PdfSignatureAppearance sap;
	private String certPath;
	private String certPwd;
	private ByteArrayOutputStream encryptByteOut;

	public PdfSignature(String infile, String outile,
			String certPath, String certPwd,KeyExecute keyExecute) throws Exception {
		super(infile, outile,keyExecute);
		this.reader = new PdfReader(this.inFile);
		this.ops = new FileOutputStream(this.outFile);
		this.encryptByteOut = new ByteArrayOutputStream();
		this.stamper = PdfStamper.createSignature(reader, encryptByteOut, '\0');
		this.certPath = certPath;
		this.certPwd = certPwd;
	}

	/**
	 * @param srcFile原文档
	 * @param outFile加密后的文档
	 * @param certPath证书路径
	 * @param certPwd证书密码
	 * @param signImg签名图片
	 * @throws DocumentException
	 * @throws Exception
	 */
	@Override
	public void seal(IbmsSign ibmsSign) throws Exception {
		KeyStoreFactory ksFactory = KeyStoreFactory.getInstance();
		try {
			// 初始化证书
			ksFactory.initKeyStore(certPath, certPwd);
			// 创建签名对象
			sap = stamper.getSignatureAppearance();
			// Sets the cryptographic parameters.
			sap.setCrypto(ksFactory.getPrivateKey(certPwd),
					ksFactory.getCertificateChain(), null,
					PdfSignatureAppearance.WINCER_SIGNED);
			this.sign();
			ops.write(encryptByteOut.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(reader!=null){
				reader.close();
			}
			if(stamper!=null){
				stamper.close();
			}
			if(ops!=null){
				ops.close();
			}
		}
	}

	private void sign() throws Exception {
		int total = reader.getNumberOfPages() + 1;
		for (int i = 1; i < total; i++) {
			this.setCreentPage(i);
			PdfPositionEngine pdfPositionEngine = new PdfPositionEngine(this);
			// stamper.addSignature("张新光",1,10,10,10,10);
			// 根据pdf 当前页，定位坐标
			List<KeyPosition> postions = pdfPositionEngine.getPosition();
			// 根据定位的坐标添加图片签章
			for (KeyPosition keyPosition : postions) {
				// 创建图片对象
				Image image = Image.getInstance(keyPosition.getImgPath()); // 使用png格式透明图片
				sap.setSignatureGraphic(image);
				sap.setAcro6Layers(true);
				sap.setRenderingMode(RenderingMode.GRAPHIC);
				sap.setReason("");// 签名原因
				sap.setLocation("");//
				sap.setCertificationLevel(PdfSignatureAppearance.CERTIFIED_FORM_FILLING_AND_ANNOTATIONS);
				// 插入图片
				float x = keyPosition.getX();
				float y = keyPosition.getY();
				float width = keyPosition.getImage_width();
				float hight = keyPosition.getImage_height();
				//sap.setVisibleSignature(new Rectangle(72, 732, 144, 780), 1, i+"_sign");
				sap.setVisibleSignature(new Rectangle(x, y, width, hight), i,i+"_sign");
			}
		}
	}

	/**
	 * 证书加密解密（不能对文件加密，只能对一小段字符加密解密）
	 * 
	 * @param certPath
	 *            证书路径
	 * @param password
	 *            证书密码
	 */
	public void encrypAndDecryption(String certPath, String certPassWord,
			String encryptData) {
		// 此类表示密钥和证书的存储设施
		KeyStore keyStore;
		String alias = "";
		// String testEncrypt = "certificate encrypt decryption";
		System.out.println("加密前: " + encryptData);
		try {
			FileInputStream is = new FileInputStream(certPath);
			// 得到KeyStore实例
			keyStore = KeyStore.getInstance("PKCS12");
			// 从指定的输入流中加载此 KeyStore。
			keyStore.load(is, certPassWord.toCharArray());
			is.close();
			// 获取keyStore别名
			alias = (String) keyStore.aliases().nextElement();
			Certificate cert = keyStore.getCertificate(alias);
			// 根据给定别名获取相关的私钥
			PrivateKey priKey = (PrivateKey) keyStore.getKey(alias,
					certPassWord.toCharArray());
			// 获取证书的公钥
			PublicKey pubKey = cert.getPublicKey();
			// 获取Cipher的实例 getInstance(算法/模式/填充)或getInstance("算法")
			Cipher cipher = Cipher.getInstance("RSA");// 
			// 公钥初始化Cipher 公钥加密
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
			// 加密
			byte[] encodeEncryp = cipher.doFinal(encryptData.getBytes());
			System.out.println("加密后: " + new String(encodeEncryp));
			// encodeEncryp = cipher.doFinal(getFileContentToByte(encrypFile));
			// 私钥初始化Cipher 私钥解密
			cipher.init(Cipher.DECRYPT_MODE, priKey);
			// 解密
			byte[] encodeDecryption = cipher.doFinal(encodeEncryp);

			String content = new String(encodeDecryption);
			System.out.println("解密后: " + content + "length: "
					+ content.length());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PdfSignatureAppearance getSap() {
		return sap;
	}

	public void setSap(PdfSignatureAppearance sap) {
		this.sap = sap;
	}

	public String getCertPath() {
		return certPath;
	}

	public void setCertPath(String certPath) {
		this.certPath = certPath;
	}

	public String getCertPwd() {
		return certPwd;
	}

	public void setCertPwd(String certPwd) {
		this.certPwd = certPwd;
	}


}
