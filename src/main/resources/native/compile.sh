# 注意，本地库编译依赖的arp_request_lib.o和nf_userspace_queue.o这两个私有库需要自己编译，源码在clib库中

# 编译arp本地库
gcc -fPIC -shared com_github_JoeKerouac_nativenet_nativ_impl_NativeArpNetInterfaceImpl.c arp_request_lib.o -o libcom_github_JoeKerouac_nativenet_nativ_impl_NativeArpNetInterfaceImpl.so
# 编译netfilter本地库
gcc -fPIC -shared com_github_JoeKerouac_nativenet_nativ_impl_NativeNetFilterInterfaceImpl.c nf_userspace_queue.o -lmnl -lnetfilter_queue -o libcom_github_JoeKerouac_nativenet_nativ_impl_NativeNetFilterInterfaceImpl.so