package com.creativemd.opticmanager;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.FLOAD;
import static org.objectweb.asm.Opcodes.FRETURN;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.creativemd.creativecore.transformer.CreativeTransformer;
import com.creativemd.creativecore.transformer.Transformer;

public class OpticTransformer extends CreativeTransformer {
	
	public OpticTransformer() {
		super("opticmanager");
	}
	
	@Override
	protected void initTransformers() {
		addTransformer(new Transformer("net.minecraft.world.World") {
			
			@Override
			public void transform(ClassNode node) {
				MethodNode m = findMethod(node, "getSunBrightnessFactor", "(F)F");
				m.instructions.clear();
				m.instructions.add(new VarInsnNode(ALOAD, 0));
				m.instructions.add(new VarInsnNode(FLOAD, 1));
				m.instructions.add(new MethodInsnNode(INVOKESTATIC, "com/creativemd/opticmanager/OpticWorldUtils", "getSunBrightness", patchDESC("(Lnet/minecraft/world/World;F)F"), false));
				m.instructions.add(new InsnNode(FRETURN));
				
				m = findMethod(node, "getSunBrightnessBody", "(F)F");
				if (m != null) {
					m.instructions.clear();
					m.instructions.add(new VarInsnNode(ALOAD, 0));
					m.instructions.add(new VarInsnNode(FLOAD, 1));
					m.instructions.add(new MethodInsnNode(INVOKESTATIC, "com/creativemd/opticmanager/OpticWorldUtils", "getSunBrightnessClient", patchDESC("(Lnet/minecraft/world/World;F)F"), false));
					m.instructions.add(new InsnNode(FRETURN));
				}
			}
		});
	}
}
