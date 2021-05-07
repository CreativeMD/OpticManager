function initializeCoreMod() {
	print("Init OpticManager coremods ...")
    return {
        'renderer': {
            'target': {
                'type': 'METHOD',
				'class': 'net.minecraft.client.renderer.entity.LivingRenderer',
				'methodName': 'func_177070_b',
				'methodDesc': '(Lnet/minecraft/entity/LivingEntity;)Z'
            },
            'transformer': function(method) {
				var asmapi = Java.type('net.minecraftforge.coremod.api.ASMAPI');
				var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');
				var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
				var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
				var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
				var Opcodes = Java.type('org.objectweb.asm.Opcodes');
				
				var start = method.instructions.getFirst();
				
				method.instructions.insertBefore(start, new LabelNode());
				method.instructions.insertBefore(start, new VarInsnNode(Opcodes.ALOAD, 1));
				method.instructions.insertBefore(start, asmapi.buildMethodCall("team/creative/opticmanager/OpticEventHandler", "shouldHideNames", "(Lnet/minecraft/entity/Entity;)Z", asmapi.MethodType.STATIC));
				
				
				method.instructions.insertBefore(start, new JumpInsnNode(Opcodes.IFEQ, start));

				method.instructions.insertBefore(start, new LabelNode());
				method.instructions.insertBefore(start, new InsnNode(Opcodes.ICONST_0));
				method.instructions.insertBefore(start, new InsnNode(Opcodes.IRETURN));
				
                return method;
            }
		}
    }
}
