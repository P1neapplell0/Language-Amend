package com.p1nero.lang_amend.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.p1nero.lang_amend.LanguageAmendConfig;
import com.p1nero.lang_amend.LanguageAmendMod;
import net.minecraft.client.resources.language.ClientLanguage;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(ClientLanguage.class)
public class ClientLanguageMixin {

    /**
     * 原来只加载en_us和选定语言的，现在给它中间插入一下就好了hhh
     */
    @WrapMethod(method = "loadFrom")
    private static ClientLanguage lang_amend$loadFrom(ResourceManager resourceManager, List<String> strings, boolean b, Operation<ClientLanguage> original) {
        Map<String, List<String>> config = LanguageAmendConfig.getConfig();
        if(strings.size() > 1) {
            String defaultLang = strings.get(0);
            String selectedLang = strings.get(1);
            if(config.containsKey(selectedLang)) {
                strings = new ArrayList<>();
                strings.add(defaultLang);
                strings.addAll(config.get(selectedLang));
                strings.add(selectedLang);
                LanguageAmendMod.LOGGER.info("检测到需修复的语言: {} ，即将根据配置添加回退语言，新列表顺序: {}", selectedLang, strings);
            }
        }
        return original.call(resourceManager, strings, b);
    }
}
