package com.j4sc.bjt.user.dao;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.j4sc.common.util.AESUtil;
import com.j4sc.common.util.JdbcUtil;
import com.j4sc.common.util.StringUtil;
import com.j4sc.common.util.VelocityUtil;
import org.apache.commons.lang.ObjectUtils;
import org.apache.velocity.VelocityContext;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description:  比公共类库下的MybatisPlusGeneratorUtil.java多了gc.setIdType(IdType.UUID);可删除该类
 * @Author: chengyz
 * @CreateDate: 2018/4/2 0002 下午 3:23
 * @Version: 1.0
 **/
public class MybatisPlusGeneratorUtil {
    private static String service_vm = "/template/Service.vm";
    private static String serviceImpl_vm = "/template/ServiceImpl.vm";

    public MybatisPlusGeneratorUtil() {
    }

    public static void generator(String jdbcDriver, String jdbcUrl, String jdbcUsername, String jdbcPassword, String module, String database, String tablePrefix, String packageName, Map<String, String> lastInsertIdTables) throws Exception {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            service_vm = com.j4sc.common.util.MybatisPlusGeneratorUtil.class.getResource(service_vm).getPath().replaceFirst("/", "");
            serviceImpl_vm = com.j4sc.common.util.MybatisPlusGeneratorUtil.class.getResource(serviceImpl_vm).getPath().replaceFirst("/", "");
        } else {
            service_vm = com.j4sc.common.util.MybatisPlusGeneratorUtil.class.getResource(service_vm).getPath();
            serviceImpl_vm = com.j4sc.common.util.MybatisPlusGeneratorUtil.class.getResource(serviceImpl_vm).getPath();
        }

        String targetProject = module + "/" + module + "-dao";
        String basePath = com.j4sc.common.util.MybatisPlusGeneratorUtil.class.getResource("/").getPath().replace("/target/classes/", "").replace(targetProject, "").replaceFirst("/", "");
        targetProject = basePath + targetProject;
        String sql = "SELECT table_name FROM INFORMATION_SCHEMA.TABLES WHERE table_schema = '" + database + "' AND table_name LIKE '" + tablePrefix + "_%';";
        System.out.println("========== 开始生成generatorConfig.xml文件 ==========");
        List<Map<String, Object>> tables = new ArrayList();
        ArrayList tablesName = new ArrayList();

        try {
            new VelocityContext();
            JdbcUtil jdbcUtil = new JdbcUtil(jdbcDriver, jdbcUrl, jdbcUsername, AESUtil.aesDecode(jdbcPassword));
            List<Map> result = jdbcUtil.selectByParams(sql, (List)null);
            Iterator var19 = result.iterator();

            while(var19.hasNext()) {
                Map map = (Map)var19.next();
                System.out.println(map.get("TABLE_NAME"));
                Map<String, Object> table = new HashMap(2);
                table.put("table_name", map.get("TABLE_NAME"));
                tablesName.add((String)table.get("table_name"));
                table.put("model_name", StringUtil.lineToHump(ObjectUtils.toString(map.get("TABLE_NAME"))));
                tables.add(table);
            }

            jdbcUtil.release();
            deleteDir(new File(targetProject + "/src/main/java/" + packageName.replaceAll("\\.", "/") + "/dao/entity"));
            deleteDir(new File(targetProject + "/src/main/java/" + packageName.replaceAll("\\.", "/") + "/dao/mapper"));
        } catch (Exception var31) {
            var31.printStackTrace();
        }

        System.out.println("========== 结束生成generatorConfig.xml文件 ==========");
        System.out.println("========== 开始运行MybatisGenerator ==========");
        AutoGenerator mpg = new AutoGenerator();
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(targetProject + "/src/main/java/");
        gc.setFileOverride(true);
        gc.setOpen(false);
        gc.setActiveRecord(true);
        gc.setEnableCache(false);
        gc.setBaseResultMap(true);
        gc.setBaseColumnList(false);
        gc.setAuthor("LongRou");
        //gc.setIdType(IdType.UUID);
        mpg.setGlobalConfig(gc);
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setTypeConvert(new MySqlTypeConvert() {
            public DbColumnType processTypeConvert(String fieldType) {
                System.out.println("转换类型：" + fieldType);
                return super.processTypeConvert(fieldType);
            }
        });
        dsc.setDriverName(jdbcDriver);
        dsc.setUsername(jdbcUsername);
        dsc.setPassword(AESUtil.aesDecode(jdbcPassword));
        dsc.setUrl(jdbcUrl);
        mpg.setDataSource(dsc);
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setInclude((String[])tablesName.toArray(new String[0]));
        mpg.setStrategy(strategy);
        PackageConfig pc = new PackageConfig();
        pc.setParent(packageName + ".dao");
        mpg.setPackageInfo(pc);
        TemplateConfig tc = new TemplateConfig();
        tc.setController((String)null);
        tc.setService((String)null);
        tc.setServiceImpl((String)null);
        mpg.setTemplate(tc);
        mpg.execute();
        System.out.println("========== 结束运行MybatisGenerator ==========");
        System.out.println("========== 开始生成Service ==========");
        String ctime = (new SimpleDateFormat("yyyy/M/d")).format(new Date());
        String servicePath = basePath + module + "/" + module + "-rest/src/main/java/" + packageName.replaceAll("\\.", "/") + "/rest/api";
        String serviceImplPath = basePath + module + "/" + module + "-server/src/main/java/" + packageName.replaceAll("\\.", "/") + "/server/rest/service/impl";

        for(int i = 0; i < tables.size(); ++i) {
            String model = StringUtil.lineToHump(ObjectUtils.toString(((Map)tables.get(i)).get("table_name")));
            String service = servicePath + "/" + model + "Service.java";
            String serviceImpl = serviceImplPath + "/" + model + "ServiceImpl.java";
            File serviceFile = new File(service);
            if (!serviceFile.exists()) {
                VelocityContext context = new VelocityContext();
                context.put("package_name", packageName);
                context.put("model", model);
                context.put("ctime", ctime);
                VelocityUtil.generate(service_vm, service, context);
                System.out.println(service);
            }

            File serviceImplFile = new File(serviceImpl);
            if (!serviceImplFile.exists()) {
                VelocityContext context = new VelocityContext();
                context.put("package_name", packageName);
                context.put("model", model);
                context.put("mapper", StringUtil.toLowerCaseFirstOne(model));
                context.put("ctime", ctime);
                VelocityUtil.generate(serviceImpl_vm, serviceImpl, context);
                System.out.println(serviceImpl);
            }
        }

        System.out.println("========== 结束生成Service ==========");
    }

    public static void deleteDir(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();

            for(int i = 0; i < files.length; ++i) {
                deleteDir(files[i]);
            }
        }

        dir.delete();
    }
}

