package com.sparrow.spring.starter.servlet;

import com.sparrow.protocol.constant.Constant;
import com.sparrow.support.CaptchaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class CaptchaServlet extends HttpServlet {
    private static Logger logger = LoggerFactory.getLogger(CaptchaServlet.class);

    private CaptchaService captchaService;

    public CaptchaServlet(CaptchaService captchaService) {
        super();
        this.captchaService = captchaService;
    }

    private static final long serialVersionUID = -5813134629255375160L;
    private int imageWidth = 100;
    private int imageHeight = 35;
    private int codeCount = 4;
    private int fontHeight;
    private int codeY;
    private int codeWidth;
    char[] codeSequence = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K',
            'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', '3',
            '4', '5', '6', '7', '8', '9'};

    @Override
    public void init() throws ServletException {
        String strWidth = this.getInitParameter("width");
        String strHeight = this.getInitParameter("height");
        String strCodeCount = this.getInitParameter("codeCount");
        try {
            if (strWidth != null && strWidth.length() != 0) {
                this.imageWidth = Integer.parseInt(strWidth);
            }
            if (strHeight != null && strHeight.length() != 0) {
                this.imageHeight = Integer.parseInt(strHeight);
            }
            if (strCodeCount != null && strCodeCount.length() != 0) {
                codeCount = Integer.parseInt(strCodeCount);
            }
        } catch (NumberFormatException e) {
            logger.error("validate code error", e);
        }
        this.codeWidth = this.imageWidth / (codeCount + 1);
        this.fontHeight = this.imageHeight - 2;
        this.codeY = this.imageHeight - 4;

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        BufferedImage buffImg = new BufferedImage(this.imageWidth,
                this.imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D gd = buffImg.createGraphics();
        Random random = new Random();
        gd.setColor(Color.white);
        gd.fillRect(0, 0, this.imageWidth, this.imageHeight);
        Font font = new Font("Fixedsys", Font.PLAIN, fontHeight);
        gd.setFont(font);
        gd.drawRect(0, 0, this.imageWidth - 1, this.imageHeight - 1);

        StringBuilder randomCode = new StringBuilder();
        int red = 0, green = 0, blue = 0;
        for (int i = 0; i < codeCount; i++) {
            String strRand = String.valueOf(codeSequence[random
                    .nextInt(this.codeSequence.length)]);
            red = random.nextInt(254);
            green = random.nextInt(254);
            blue = random.nextInt(254);
            gd.setColor(new Color(red, green, blue));
            gd.drawString(strRand, (i * this.codeWidth) + this.codeWidth / 2,
                    codeY);
            randomCode.append(strRand);
        }
        this.captchaService.setCaptcha(req.getRequestedSessionId(), randomCode.toString());
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setDateHeader("Expires", 0);
        resp.setContentType("image/jpg");
        ServletOutputStream sos = resp.getOutputStream();
        ImageIO.write(buffImg, "jpeg", sos);
        sos.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        if (req.getParameter(Constant.VALIDATE_CODE) != null
                && req.getParameter(Constant.VALIDATE_CODE).equalsIgnoreCase(req.getSession().getAttribute(
                        Constant.VALIDATE_CODE)
                .toString().toLowerCase())) {
            resp.getWriter().write("OK");
            return;
        }
        resp.getWriter().write("false");
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
